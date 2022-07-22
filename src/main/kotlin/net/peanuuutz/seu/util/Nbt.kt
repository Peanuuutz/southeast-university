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

@file:JvmName("NbtUtils")

package net.peanuuutz.seu.util

import net.minecraft.entity.EntityType
import net.minecraft.item.ItemStack
import kotlin.jvm.optionals.getOrNull

val ItemStack.vehicleId: String?
    get() = getSubTag("Vehicle")
        ?.getString("id")

@OptIn(ExperimentalStdlibApi::class)
val ItemStack.vehicleType: EntityType<*>?
    get() = vehicleId
        ?.let { id -> EntityType.get(id) }
        ?.getOrNull()