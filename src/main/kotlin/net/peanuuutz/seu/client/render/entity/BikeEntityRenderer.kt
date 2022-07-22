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
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.peanuuutz.seu.Seu
import net.peanuuutz.seu.entity.BikeEntity
import net.peanuuutz.seu.util.TAU
import net.peanuuutz.seu.util.modelPart
import net.peanuuutz.seu.util.toRadian

class BikeEntityRenderer(
    dispatcher: EntityRenderDispatcher
) : VehicleEntityRenderer<BikeEntity, BikeEntityModel>(
    dispatcher = dispatcher,
    texture = Identifier(Seu.MOD_ID, "textures/entity/bike.png"),
    model = BikeEntityModel
) {
    override val modelScale: Float get() = 0.6f

    override val modelYOffset: Double get() = 1.0
}

object BikeEntityModel : VehicleEntityModel<BikeEntity>(
    textureWidth = 128,
    textureHeight = 128
) {
    // region model parts

    private val body: ModelPart = modelPart(this) {
        setPivot(0.0F, 24.0F, 0.0F)
        addChild {
            setPivot(0.0834F, -10.0392F, -6.2054F)
            setRotation(-0.1745F, 0.0F, 0.0F)
            addCuboid(30, 16, -0.0834F, 4.0F, -10.0F, 1.0F, 1.0F, 11.0F)
            addCuboid(17, 20, -2.0834F, 4.0F, -10.0F, 1.0F, 1.0F, 11.0F)
        }
        addChild {
            setPivot(0.9431F, -18.7829F, -6.1146F)
            setRotation(0.8727F, 0.0F, 0.0F)
            addCuboid(0, 20, -1.4431F, -1.1095F, -15.2205F, 1.0F, 1.0F, 15.0F)
            addCuboid(21, 0, -2.4431F, -1.1095F, -15.2205F, 1.0F, 1.0F, 15.0F)
        }
        addChild {
            setPivot(0.0F, -6.0F, 15.0F)
            setRotation(0.2182F, 0.0F, 0.0F)
            addCuboid(21, 6, -2.0F, -19.0F, 0.0F, 3.0F, 5.0F, 2.0F)
        }
        addChild {
            setPivot(0.0F, -11.0F, -5.0F)
            setRotation(0.1745F, 0.0F, 0.0F)
            addCuboid(0, 0, -2.0F, -10.0F, -2.0F, 3.0F, 14.0F, 3.0F)
        }
        addChild {
            setPivot(-0.5544F, -22.1051F, 11.8689F)
            setRotation(-0.7055F, -0.0769F, 0.0073F)
            addCuboid(32, 32, -0.5F, -0.7708F, -1.1913F, 1.0F, 22.0F, 2.0F)
        }
        addChild {
            setPivot(0.0F, -21.75F, 8.75F)
            setRotation(0.2183F, -0.0057F, 0.003F)
            addCuboid(0, 0, -1.0F, -1.0682F, -16.0049F, 1.0F, 1.0F, 19.0F)
        }
        addChild {
            setPivot(0.0F, -11.0F, 0.0F)
            addCuboid(21, 0, -2.0F, -15.25F, -10.0F, 3.0F, 2.0F, 4.0F)
            addCuboid(0, 30, -1.0F, -15.0F, -8.0F, 1.0F, 1.0F, 4.0F)
            addChild {
                setPivot(0.0F, 0.0F, -5.0F)
                setRotation(0.1745F, 0.0F, 0.0F)
                addCuboid(12, 0, -1.0F, -15.0F, -1.0F, 1.0F, 15.0F, 1.0F)
            }
        }
        addChild {
            setPivot(0.9456F, -5.6051F, 1.3689F)
            addCuboid(0, 20, -0.5F, -2.5F, -7.5F, 1.0F, 5.0F, 5.0F)
            addCuboid(38, 0, -0.5F, -1.5F, -8.5F, 1.0F, 3.0F, 7.0F)
            addCuboid(17, 20, -0.5F, -3.5F, -6.5F, 1.0F, 7.0F, 3.0F)
            addCuboid(0, 36, -3.4456F, -2.5F, -7.5F, 4.0F, 5.0F, 5.0F)
        }
    }

    private val pedal: ModelPart = modelPart(this) {
        setPivot(0.0F, 18.4F, -3.65F)
        addCuboid(17, 94, -4.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F)
        addCuboid(24, 108, 2.0F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F)
        addCuboid(24, 108, -4.0F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, mirror = true)
        addCuboid(29, 111, 3.0F, 3.0F, -1.0F, 3.0F, 1.0F, 2.0F)
        addCuboid(29, 111, -7.0F, -4.0F, -1.0F, 3.0F, 1.0F, 2.0F, mirror = true)
    }

    private val frontWheel: ModelPart = modelPart(this) {
        setPivot(-0.0044F, 14.2136F, 3.7332F)
        addCuboid(21, 13, -1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F)
        addCuboid(9, 47, -0.5F, -8.4497F, -3.5F, 1.0F, 1.0F, 7.0F)
        addCuboid(11, 39, -0.5F, 7.4497F, -3.5F, 1.0F, 1.0F, 7.0F)
        addCuboid(66, 3, 0.0F, -8.4997F, -8.55F, 0.0F, 17.0F, 17.0F)
        addChild {
            setPivot(0.0F, -7.0355F, -4.2071F)
            setRotation(-2.3562F, 0.0F, 0.0F)
            addCuboid(43, 16, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, -2.0F, -7.9497F)
            setRotation(-1.5708F, 0.0F, 0.0F)
            addCuboid(38, 28, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 4.2071F, -7.0355F)
            setRotation(-0.7854F, 0.0F, 0.0F)
            addCuboid(38, 36, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 7.0355F, 4.2071F)
            setRotation(-2.3562F, 0.0F, 0.0F)
            addCuboid(38, 44, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 2.0F, 7.9497F)
            setRotation(-1.5708F, 0.0F, 0.0F)
            addCuboid(0, 46, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, -4.2071F, 7.0355F)
            setRotation(-0.7854F, 0.0F, 0.0F)
            addCuboid(47, 3, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
    }

    private val front: ModelPart = modelPart(this) {
        setPivot(-0.4956F, 2.4862F, 12.0168F)
        +frontWheel
        addChild {
            setPivot(0.0F, 0.0F, 0.0F)
            addChild {
                setPivot(-2.6272F, -5.0832F, -0.3194F)
                setRotation(0.3688F, -0.9269F, -0.2998F)
                addCuboid(0, 20, -0.5F, -1.75F, 1.5F, 1.0F, 3.0F, 1.0F)
            }
            addChild {
                setPivot(3.4376F, -5.2073F, 0.2406F)
                setRotation(0.3688F, 0.9269F, 0.2998F)
                addCuboid(21, 0, -0.5F, -1.75F, 0.5F, 1.0F, 3.0F, 1.0F)
            }
            addChild {
                setPivot(-4.2655F, -6.3077F, 0.5842F)
                setRotation(0.2643F, 0.5944F, 0.1504F)
                addCuboid(11, 16, -0.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F)
            }
            addChild {
                setPivot(4.2567F, -6.3077F, 0.5842F)
                setRotation(0.2643F, -0.5944F, -0.1504F)
                addCuboid(7, 20, -2.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F)
            }
            addChild {
                setPivot(-0.0044F, -6.0386F, -0.6299F)
                setRotation(0.2182F, 0.0F, 0.0F)
                addCuboid(0, 17, -2.5F, -0.5F, -0.6F, 5.0F, 1.0F, 1.0F)
            }
            addChild {
                setPivot(0.9956F, 14.2136F, 2.7332F)
                setRotation(0.2182F, 0.0F, 0.0F)
                addCuboid(47, 56, -0.5F, -10.0F, 0.5F, 1.0F, 10.0F, 1.0F)
                addCuboid(51, 56, -2.5F, -10.0F, 0.5F, 1.0F, 10.0F, 1.0F)
                addCuboid(27, 36, -1.5F, -21.25F, 0.5F, 1.0F, 11.0F, 1.0F)
            }
            addChild {
                setPivot(1.7284F, 3.7381F, 1.1791F)
                setRotation(0.1415F, 0.1666F, -0.8608F)
                addCuboid(22, 20, -1.4F, -0.6F, -0.25F, 1.0F, 1.0F, 1.0F)
            }
            addChild {
                setPivot(-1.7051F, 3.7381F, 1.1791F)
                setRotation(0.1415F, -0.1666F, 0.8608F)
                addCuboid(28, 17, 0.4F, -0.6F, -0.25F, 1.0F, 1.0F, 1.0F)
            }
            addChild {
                setPivot(0.0117F, 14.0742F, 2.7023F)
                setRotation(0.2182F, 0.0F, 0.0F)
                addCuboid(7, 22, -1.0F, -10.5F, 0.5F, 2.0F, 1.0F, 1.0F)
            }
        }
    }

    private val backWheel: ModelPart = modelPart(this) {
        setPivot(-0.5F, 16.6997F, -16.25F)
        addCuboid(21, 16, -1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F)
        addCuboid(30, 20, -0.5F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F)
        addCuboid(0, 54, -0.5F, -8.4497F, -3.5F, 1.0F, 1.0F, 7.0F)
        addCuboid(47, 48, -0.5F, 7.4497F, -3.5F, 1.0F, 1.0F, 7.0F)
        addCuboid(66, 3, 0.0F, -8.4997F, -8.55F, 0.0F, 17.0F, 17.0F)
        addChild {
            setPivot(0.0F, -7.0355F, -4.2071F)
            setRotation(-2.3562F, 0.0F, 0.0F)
            addCuboid(47, 24, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, -2.0F, -7.9497F)
            setRotation(-1.5708F, 0.0F, 0.0F)
            addCuboid(47, 32, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 4.2071F, -7.0355F)
            setRotation(-0.7854F, 0.0F, 0.0F)
            addCuboid(47, 40, -0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 7.0355F, 4.2071F)
            setRotation(-2.3562F, 0.0F, 0.0F)
            addCuboid(18, 49, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, 2.0F, 7.9497F)
            setRotation(-1.5708F, 0.0F, 0.0F)
            addCuboid(52, 11, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
        addChild {
            setPivot(0.0F, -4.2071F, 7.0355F)
            setRotation(-0.7854F, 0.0F, 0.0F)
            addCuboid(31, 52, -0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 7.0F)
        }
    }

    // endregion

    override val stableParts: List<ModelPart> = listOf(body)

    override fun renderUnstableParts(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
        static: Boolean
    ) {
        // Store states
        val previousPedalPitch = pedal.pitch
        val previousFrontWheelPitch = frontWheel.pitch
        val previousBackWheelPitch = backWheel.pitch
        // Modify model parts
        if (static) {
            pedal.pitch = 0f
            frontWheel.pitch = 0f
            front.yaw = turningDegree.toRadian() // QUESTION: should it be changed?
            backWheel.pitch = 0f
        } else {
            val wheelRollAngle = currentSpeed / 2 // MAGIC CONSTANT: by testing
            pedal.pitch = (previousPedalPitch - wheelRollAngle / 2) % TAU // MAGIC CONSTANT: by definition
            frontWheel.pitch = (previousFrontWheelPitch - wheelRollAngle) % TAU
            front.yaw = turningDegree.toRadian()
            backWheel.pitch = (previousBackWheelPitch - wheelRollAngle) % TAU
        }
        // Render
        pedal.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        front.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        backWheel.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        // Restore states
        if (static) {
            pedal.pitch = previousPedalPitch
            frontWheel.pitch = previousFrontWheelPitch
            backWheel.pitch = previousBackWheelPitch
        }
    }
}
