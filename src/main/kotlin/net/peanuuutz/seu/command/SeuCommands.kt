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

package net.peanuuutz.seu.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import net.peanuuutz.seu.SeuConfigOwner

private typealias SubCommand = LiteralArgumentBuilder<ServerCommandSource>

private val loadCommand: SubCommand = CommandManager.literal("load")
    .requires { source -> source.hasPermissionLevel(3) }
    .executes { context ->
        val success = SeuConfigOwner.load()
        try {
            val key = if (success) "message.seu.load_succeed" else "message.seu.load_fail"
            context.source.player.sendMessage(TranslatableText(key), true)
        } catch (_: CommandSyntaxException) {
        }
        if (success) 1 else 0
    }

fun registerCommands() {
    CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
        val root = CommandManager.literal("seu")
            .then(loadCommand)
        dispatcher.register(root)
    }
}