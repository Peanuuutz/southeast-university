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

package net.peanuuutz.seu.client.render.entity

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f
import net.peanuuutz.seu.entity.VehicleEntity
import net.peanuuutz.seu.util.calculateHorizontalSpeed

abstract class VehicleEntityRenderer<T : VehicleEntity, M : VehicleEntityModel<T>>(
    dispatcher: EntityRenderDispatcher,
    private val texture: Identifier,
    private val model: M
) : EntityRenderer<T>(dispatcher) {
    open val modelScale: Float = 1f

    abstract val modelYOffset: Double

    override fun render(
        entity: T,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        render(
            turningDegree = entity.turningDegree,
            currentSpeed = calculateHorizontalSpeed(entity.velocity, entity.yaw).toFloat(),
            yaw = yaw,
            matrices = matrices,
            vertexConsumers = vertexConsumers,
            light = light
        )
    }

    fun render(
        turningDegree: Float,
        currentSpeed: Float,
        yaw: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int = OverlayTexture.DEFAULT_UV,
        static: Boolean = false
    ) {
        // Modify model
        model.turningDegree = turningDegree
        model.currentSpeed = currentSpeed
        // Render
        matrices.push()
        matrices.translate(0.0, modelYOffset, 0.0)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw))
        val modelScale = modelScale
        matrices.scale(-modelScale, -modelScale, modelScale)
        val buffer = vertexConsumers.getBuffer(model.getLayer(texture))
        model.render(matrices, buffer, light, overlay, 1f, 1f, 1f, 1f, static)
        matrices.pop()
    }

    final override fun getTexture(entity: T): Identifier = texture
}

abstract class VehicleEntityModel<T : VehicleEntity>(
    textureWidth: Int,
    textureHeight: Int,
    layerGetter: (Identifier) -> RenderLayer = RenderLayer::getEntityCutoutNoCull
) : EntityModel<T>(layerGetter) {
    var turningDegree: Float = 0f
    var currentSpeed: Float = 0f

    init {
        this.textureWidth = textureWidth
        this.textureHeight = textureHeight
    }

    final override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ): Unit = render(matrices, vertices, light, overlay, red, green, blue, alpha, true)

    fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        static: Boolean = false
    ) {
        for (index in stableParts.indices) {
            stableParts[index].render(matrices, vertices, light, overlay, red, green, blue, alpha)
        }
        renderUnstableParts(matrices, vertices, light, overlay, red, green, blue, alpha, static)
    }

    abstract val stableParts: List<ModelPart>

    abstract fun renderUnstableParts(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        static: Boolean = false
    )

    final override fun setAngles(
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {}
}