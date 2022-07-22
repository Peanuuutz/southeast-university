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

@file:JvmName("SeuEntities")

package net.peanuuutz.seu.entity

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.peanuuutz.seu.Seu

@JvmField
val BIKE_ENTITY: EntityType<BikeEntity> = FabricEntityTypeBuilder
    .create(SpawnGroup.MISC) { _, world -> BikeEntity(world = world) }
    .dimensions(EntityDimensions.fixed(0.3f, 1f))
    .build()

val entityTypes: Map<String, EntityType<*>> = mapOf(
    BikeEntity.ID to BIKE_ENTITY
)

fun registerEntityTypes() {
    for ((id, vehicleType) in entityTypes) {
        Registry.register(
            Registry.ENTITY_TYPE,
            Identifier(Seu.MOD_ID, id),
            vehicleType
        )
    }
}