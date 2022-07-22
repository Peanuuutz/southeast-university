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

@file:JvmName("SeuItems")

package net.peanuuutz.seu.item

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.peanuuutz.seu.Seu
import net.peanuuutz.seu.entity.entityTypes

val items: Map<String, Item> = mapOf(
    "vehicle_block" to VehicleBlockItem
)

@JvmField
val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder
    .create(Identifier(Seu.MOD_ID, "general"))
    .icon { ItemStack(Items.CONDUIT) }
    .appendItems { stackSupplier ->
        for ((_, item) in items) {
            if (item is VehicleBlockItem) {
                stackSupplier.appendVehicleBlocks()
                continue
            }
            stackSupplier.add(ItemStack(item))
        }
    }
    .build()

private fun MutableList<ItemStack>.appendVehicleBlocks() {
    for ((_, entityType) in entityTypes) {
        val stack = ItemStack(VehicleBlockItem).apply {
            val vehicleNbt = getOrCreateSubTag("Vehicle")
            vehicleNbt.putString("id", EntityType.getId(entityType).toString())
        }
        add(stack)
    }
}

fun registerItems() {
    for ((id, item) in items) {
        Registry.register(
            Registry.ITEM,
            Identifier(Seu.MOD_ID, id),
            item
        )
    }
}