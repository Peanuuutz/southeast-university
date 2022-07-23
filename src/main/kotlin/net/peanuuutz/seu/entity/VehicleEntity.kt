/*
    Copyright 2022 Peanuuutz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package net.peanuuutz.seu.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.MovementType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.tag.BlockTags
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.peanuuutz.seu.SeuConfig.CommonVehicleProperties
import net.peanuuutz.seu.SeuConfig.UniqueVehicleProperties
import net.peanuuutz.seu.SeuConfigOwner
import net.peanuuutz.seu.util.calculateVelocity
import net.peanuuutz.seu.util.toRadian
import net.peanuuutz.seu.util.calculateHorizontalSpeed

abstract class VehicleEntity(
    world: World,
    entityType: EntityType<*>
) : Entity(entityType, world) {
    private val commonProperties: CommonVehicleProperties get() = SeuConfigOwner.config.commonVehicleProperties

    abstract val uniqueProperties: UniqueVehicleProperties

    init {
        inanimate = true
    }

    override fun initDataTracker() {
        dataTracker.startTracking(TURNING_DEGREE, 0f)
    }

    final override fun createSpawnPacket(): Packet<*> = EntitySpawnS2CPacket(this)

    var turningDegree: Float
        get() = dataTracker[TURNING_DEGREE]
        set(value) {
            dataTracker[TURNING_DEGREE] = value.coerceIn(-uniqueProperties.maxTurningDegree, uniqueProperties.maxTurningDegree)
        }

    private var yawDelta: Float = 0f

    override fun tick() {
        baseTick()
        updateFromTracked()
        if (isLogicalSideForUpdatingMovement) {
            val horizontalSpeed = if (world.isClient) {
                updateTurningOnClient()
                calculateHorizontalSpeedOnClient()
            } else {
                updateTurningOnServer()
                calculateHorizontalSpeedOnServer()
            }
            yawDelta = turningDegree * horizontalSpeed.toFloat()
            yaw = (yaw + yawDelta) % 360f
            velocity = calculateVelocity(horizontalSpeed, velocity.y + verticalAcceleration, yaw)
            velocityDirty = true
            move(MovementType.SELF, velocity)
        } else {
            velocity = Vec3d.ZERO
        }
    }

    private var trackedX: Double = 0.0
    private var trackedY: Double = 0.0
    private var trackedZ: Double = 0.0
    private var trackedPitch: Float = 0f
    private var trackedYaw: Float = 0f
    private var tickTracking: Int = 0

    private fun updateFromTracked() {
        if (isLogicalSideForUpdatingMovement) {
            tickTracking = 0
            updateTrackedPosition(x, y, z)
        }
        if (tickTracking <= 0) {
            return
        }
        val delta = 1.0 / tickTracking
        val actualX = MathHelper.lerp(delta, x, trackedX)
        val actualY = MathHelper.lerp(delta, y, trackedY)
        val actualZ = MathHelper.lerp(delta, z, trackedZ)
        pitch = MathHelper.lerp(delta.toFloat(), pitch, trackedPitch) % 360f
        yaw = MathHelper.lerp(delta.toFloat(), yaw, trackedYaw) % 360f
        setPosition(actualX, actualY, actualZ)
        tickTracking--
    }

    override fun updateTrackedPositionAndAngles(
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
        interpolationSteps: Int,
        interpolate: Boolean
    ) {
        trackedX = x
        trackedY = y
        trackedZ = z
        trackedPitch = pitch
        trackedYaw = yaw
        tickTracking = 10
    }

    var pressingLeft: Boolean = false
    var pressingRight: Boolean = false
    var pressingForward: Boolean = false
    var pressingBack: Boolean = false

    private fun calculateHorizontalSpeedOnClient(): Double {
        val prevHorizontalSpeed = calculateHorizontalSpeed(velocity, prevYaw)
        return when {
            world.getBlockState(velocityAffectingPos).block in BlockTags.ICE -> prevHorizontalSpeed
            pressingForward -> {
                when {
                    prevHorizontalSpeed > uniqueProperties.maxSpeed - uniqueProperties.acceleration -> uniqueProperties.maxSpeed
                    prevHorizontalSpeed < 0f -> prevHorizontalSpeed * uniqueProperties.brakeFactor
                    else -> prevHorizontalSpeed + uniqueProperties.acceleration
                }
            }
            pressingBack -> {
                if (prevHorizontalSpeed > 0f) {
                    prevHorizontalSpeed * uniqueProperties.brakeFactor
                } else {
                    -uniqueProperties.backwardSpeed
                }
            }
            else -> prevHorizontalSpeed * commonProperties.frictionFactor
        }
    }

    private fun updateTurningOnClient() {
        if (pressingLeft) {
            turningDegree -= commonProperties.turnSpeed
        } else if (pressingRight) {
            turningDegree += commonProperties.turnSpeed
        } else {
            turningDegree *= commonProperties.retractFactor
        }
    }

    private fun calculateHorizontalSpeedOnServer(): Double {
        val prevHorizontalSpeed = calculateHorizontalSpeed(velocity, prevYaw)
        return if (world.getBlockState(velocityAffectingPos).block in BlockTags.ICE) {
            prevHorizontalSpeed
        } else {
            prevHorizontalSpeed * commonProperties.frictionFactor
        }
    }

    private fun updateTurningOnServer() {
        turningDegree *= commonProperties.retractFactor
    }

    private val verticalAcceleration: Double get() = if (hasNoGravity()) 0.0 else -0.04

    final override fun updatePassengerPosition(passenger: Entity) {
        if (passenger !in passengerList) {
            return
        }
        val passengerIndex = passengerList.indexOf(passenger)
        val passengerPosition = layoutLocalPassengerPosition(passengerIndex)
            .rotateY(-(yaw + 90f).toRadian())
            .add(x, y + passenger.heightOffset, z)
        passenger.setPosition(passengerPosition.x, passengerPosition.y, passengerPosition.z)
        passenger.yaw += yawDelta
        fixPassengerPose(passenger)
    }

    abstract fun layoutLocalPassengerPosition(index: Int): Vec3d

    private fun fixPassengerPose(passenger: Entity) {
        val deltaYaw = MathHelper.wrapDegrees(passenger.yaw - yaw)
        val coercedDeltaYaw = deltaYaw.coerceIn(-105f, 105f)
        passenger.prevYaw += coercedDeltaYaw - deltaYaw
        passenger.yaw += coercedDeltaYaw - deltaYaw
        passenger.headYaw = passenger.yaw
        passenger.setBodyYaw(yaw)
    }

    final override fun onPassengerLookAround(passenger: Entity) {
        fixPassengerPose(passenger)
    }

    final override fun getEyeHeight(pose: EntityPose, dimensions: EntityDimensions): Float = dimensions.height

    final override fun getMovementDirection(): Direction = horizontalFacing.rotateYClockwise()

    final override fun canClimb(): Boolean = false

    final override fun collidesWith(other: Entity): Boolean { // NOTE: this is for collision
        return (other.isCollidable || other.isPushable) && isConnectedThroughVehicle(other).not()
    }

    final override fun collides(): Boolean = !removed // NOTE: this is for ray-casting

    final override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        return when {
            player.shouldCancelInteraction() -> ActionResult.PASS
            hasPassengers() -> {
                if (world.isClient.not()) {
                    player.startRiding(this)
                }
                ActionResult.success(world.isClient)
            }
            world.isClient -> ActionResult.SUCCESS
            player.startRiding(this) -> ActionResult.CONSUME
            else -> ActionResult.PASS
        }
    }

    final override fun canAddPassenger(passenger: Entity): Boolean = passengerList.size < maxPassengers

    abstract val maxPassengers: Int

    final override fun getPrimaryPassenger(): Entity? = passengerList.getOrNull(0)

    override fun damage(source: DamageSource, amount: Float): Boolean {
        return when {
            world.isClient || removed -> true
            isInvulnerableTo(source) -> false
            source.shouldRemoveVehicle -> {
                removeAllPassengers()
                remove()
                true
            }
            else -> false
        }
    }

    private val DamageSource.shouldRemoveVehicle: Boolean
        get() = isSourceCreativePlayer || isOutOfWorld || isFire || isExplosive

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        turningDegree = nbt.getFloat("Turning")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        nbt.putFloat("Turning", turningDegree)
    }

    final override fun getMountedHeightOffset(): Double = 0.0 // Use layoutPassengerPosition to determine position

    companion object {
        val TURNING_DEGREE: TrackedData<Float> = DataTracker.registerData(VehicleEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
    }
}