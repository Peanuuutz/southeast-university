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

import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.peanuuutz.seu.SeuConfig.UniqueVehicleProperties
import net.peanuuutz.seu.SeuConfigOwner

class BikeEntity(
    world: World
) : VehicleEntity(world, BIKE_ENTITY) {
    override val uniqueProperties: UniqueVehicleProperties
        get() = SeuConfigOwner.config.uniqueVehicleProperties[ID] ?: DEFAULT_PROPERTIES

    override fun isPushable(): Boolean = true

    override fun layoutLocalPassengerPosition(index: Int): Vec3d = LOCAL_PASSENGER_POSITION

    override val maxPassengers: Int get() = 1

    companion object {
        const val ID: String = "bike"

        @JvmField
        val DEFAULT_PROPERTIES: UniqueVehicleProperties = UniqueVehicleProperties(
            maxSpeed = 0.5,
            acceleration = 0.01,
            brakeFactor = 0.6,
            backwardSpeed = 0.1,
            maxTurningDegree = 45f
        )

        @JvmField
        val LOCAL_PASSENGER_POSITION: Vec3d = Vec3d(-0.3, 0.8, 0.0)
    }
}