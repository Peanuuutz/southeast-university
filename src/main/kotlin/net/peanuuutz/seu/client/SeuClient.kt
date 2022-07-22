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

package net.peanuuutz.seu.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.peanuuutz.seu.Seu
import net.peanuuutz.seu.SeuConfigOwner
import net.peanuuutz.seu.client.render.block.registerBlockEntityRenderers
import net.peanuuutz.seu.client.render.entity.registerEntityRenderers
import net.peanuuutz.seu.util.SeuGlobal

object SeuClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerEntityRenderers()
        registerBlockEntityRenderers()

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            SeuConfigOwner.save()
        }

        SeuGlobal.info("Mod ${Seu.MOD_ID} client side loaded")
    }
}