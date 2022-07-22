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

import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.peanuuutz.seu.entity.BikeEntity
import net.peanuuutz.seu.util.SeuGlobal
import net.peanuuutz.tomlkt.Toml
import java.io.File

@Serializable
data class SeuConfig(
    val commonVehicleProperties: CommonVehicleProperties = CommonVehicleProperties(),
    val uniqueVehicleProperties: Map<String, UniqueVehicleProperties> = mapOf(
        BikeEntity.ID to BikeEntity.DEFAULT_PROPERTIES
    )
) {
    @Serializable
    data class CommonVehicleProperties(
        val turnSpeed: Float = 2f,
        val frictionFactor: Double = 0.75,
        val retractFactor: Float = 0.9f
    )

    @Serializable
    data class UniqueVehicleProperties(
        val maxSpeed: Double,
        val acceleration: Double,
        val brakeFactor: Double,
        val backwardSpeed: Double,
        val maxTurningDegree: Float
    )
}

object SeuConfigOwner {
    val config: SeuConfig get() = internalConfig
    private var internalConfig: SeuConfig = SeuConfig()

    private val toml = Toml {
        ignoreUnknownKeys = true
    }

    private val configFile: File = FabricLoader.getInstance().configDir.resolve("seu.toml").toFile()

    fun load(): Boolean {
        return if (configFile.isFile) {
            configFile.reader().use { reader ->
                wrapProcess("reading") {
                    internalConfig = toml.decodeFromString(SeuConfig.serializer(), reader.readText())
                }
            }
        } else {
            SeuGlobal.warn("Cannot locate config file. Creating a new one...")
            if (configFile.createNewFile().not()) {
                SeuGlobal.error("Cannot create config file! Possibly a directory with the same name exists")
            }
            save()
        }
    }

    fun save(): Boolean {
        return configFile.writer().use { writer ->
            wrapProcess("writing") {
                writer.write(toml.encodeToString(SeuConfig.serializer(), internalConfig))
            }
        }
    }

    private inline fun wrapProcess(processName: String, block: () -> Unit): Boolean {
        return try {
            block()
            true
        } catch (e: Exception) {
            SeuGlobal.error("Error occurred when $processName config")
            SeuGlobal.error(e)
            false
        }
    }
}