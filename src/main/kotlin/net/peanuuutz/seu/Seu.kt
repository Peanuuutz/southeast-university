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

package net.peanuuutz.seu

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.peanuuutz.seu.block.entity.registerBlockEntityTypes
import net.peanuuutz.seu.block.registerBlocks
import net.peanuuutz.seu.command.registerCommands
import net.peanuuutz.seu.entity.registerEntityTypes
import net.peanuuutz.seu.item.registerItems
import net.peanuuutz.seu.util.SeuGlobal

object Seu : ModInitializer {
    const val MOD_ID = "seu"

    override fun onInitialize() {
        SeuConfigOwner.load()

        registerEntityTypes()
        registerBlocks()
        registerBlockEntityTypes()
        registerItems()
        registerCommands()

        ServerLifecycleEvents.SERVER_STOPPED.register { server ->
            if (server.isDedicated) {
                SeuConfigOwner.save()
            }
        }

        SeuGlobal.info("Mod $MOD_ID loaded")
    }
}