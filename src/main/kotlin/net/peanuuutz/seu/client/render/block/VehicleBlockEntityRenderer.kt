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

package net.peanuuutz.seu.client.render.block

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.peanuuutz.seu.block.entity.VehicleBlockEntity
import net.peanuuutz.seu.client.render.entity.VehicleEntityRenderer
import net.peanuuutz.seu.util.fastCosDegree
import net.peanuuutz.seu.util.fastSinDegree

class VehicleBlockEntityRenderer(
    dispatcher: BlockEntityRenderDispatcher
) : BlockEntityRenderer<VehicleBlockEntity>(dispatcher) {
    override fun render(
        entity: VehicleBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val vehicleType = entity.vehicleType
        val renderer = MinecraftClient.getInstance()
            .entityRenderDispatcher
            .renderers[vehicleType] as? VehicleEntityRenderer<*, *>
            ?: return
        val yaw = entity.yaw
        val unitXOffset = fastCosDegree(yaw)
        val unitZOffset = fastSinDegree(yaw)
        val offsets = arrangeVehicles(entity.number, vehicleType.width.toDouble())
        matrices.translate(0.5, 0.0, 0.5)
        for (index in offsets.indices) {
            val offset = offsets[index]
            val offsetX = offset * unitXOffset
            val offsetZ = offset * unitZOffset
            matrices.translate(offsetX, 0.0, offsetZ)
            renderer.render(
                turningDegree = 0f,
                currentSpeed = 0f,
                yaw = yaw,
                matrices = matrices,
                vertexConsumers = vertexConsumers,
                light = light,
                overlay = overlay,
                static = true
            )
            matrices.translate(-offsetX, 0.0, -offsetZ)
        }
    }

    // NOTE: basically we apply space around arrangement ( -1--2--3- ) (could be varied later)
    private fun arrangeVehicles(number: Int, vehicleWidth: Double): DoubleArray {
        val result = DoubleArray(number)
        val singleGap = (1 - vehicleWidth * number) / (number * 2)
        var currentPosition = -0.5 + singleGap + vehicleWidth / 2 // NOTE: from left to right
        for (index in 0 until number) {
            result[index] = currentPosition // NOTE: distance from the center
            currentPosition += singleGap * 2 + vehicleWidth
        }
        return result
    }
}