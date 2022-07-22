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

@file:JvmName("SeuBlockEntities")

package net.peanuuutz.seu.block.entity

import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.peanuuutz.seu.Seu
import net.peanuuutz.seu.block.VehicleBlock

@JvmField
val VEHICLE_BLOCK_ENTITY: BlockEntityType<VehicleBlockEntity> = BlockEntityType.Builder
    .create(::VehicleBlockEntity, VehicleBlock)
    .build(null)

val blockEntityTypes: Map<String, BlockEntityType<*>> = mapOf(
    "vehicle_block_entity" to VEHICLE_BLOCK_ENTITY
)

fun registerBlockEntityTypes() {
    for ((id, blockEntityType) in blockEntityTypes) {
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            Identifier(Seu.MOD_ID, id),
            blockEntityType
        )
    }
}