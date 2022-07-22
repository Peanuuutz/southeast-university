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

package net.peanuuutz.seu.block.entity

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.NbtCompound
import net.peanuuutz.seu.entity.BIKE_ENTITY
import kotlin.jvm.optionals.getOrDefault

class VehicleBlockEntity : BlockEntity(VEHICLE_BLOCK_ENTITY), BlockEntityClientSerializable {
    var vehicleType: EntityType<*> = BIKE_ENTITY
        set(value) {
            if (field != value) {
                field = value
                number = number.coerceIn(1..maxNumber)
                markDirty()
            }
        }
    var yaw: Float = 0f
        set(value) {
            val new = value % 360f
            if (field != new) {
                field = new
                markDirty()
            }
        }
    var number: Int = 1
        set(value) {
            if (field != value && value in 1..maxNumber) {
                field = value
                markDirty()
            }
        }

    private val maxNumber: Int get() = (1 / vehicleType.width).toInt().coerceAtLeast(1)

    val canAdd: Boolean get() = number < maxNumber

    override fun fromTag(state: BlockState, tag: NbtCompound) {
        super.fromTag(state, tag)
        fromClientTag(tag)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun fromClientTag(tag: NbtCompound) {
        val vehicleNbt = tag.getCompound("Vehicle") ?: return
        vehicleType = EntityType.get(vehicleNbt.getString("id")).getOrDefault(BIKE_ENTITY)
        yaw = vehicleNbt.getFloat("yaw")
        number = vehicleNbt.getInt("number")
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        return toClientTag(nbt)
    }

    override fun toClientTag(tag: NbtCompound): NbtCompound {
        val vehicleNbt = NbtCompound().apply {
            putString("id", EntityType.getId(vehicleType).toString())
            putFloat("yaw", yaw)
            putInt("number", number)
        }
        tag.put("Vehicle", vehicleNbt)
        return tag
    }
}