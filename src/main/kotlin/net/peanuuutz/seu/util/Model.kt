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

package net.peanuuutz.seu.util

import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart

inline fun modelPart(
    model: Model,
    block: ModelPartBuilder.() -> Unit
): ModelPart = ModelPartBuilder(model).apply(block).delegate

class ModelPartBuilder(val model: Model) {
    val delegate: ModelPart = ModelPart(model)

    fun setPivot(x: Float, y: Float, z: Float) {
        delegate.setPivot(x, y, z)
    }

    fun setRotation(pitch: Float, yaw: Float, roll: Float) {
        delegate.pitch = pitch
        delegate.yaw = yaw
        delegate.roll = roll
    }

    fun addCuboid(
        u: Int,
        v: Int,
        x: Float,
        y: Float,
        z: Float,
        length: Float,
        width: Float,
        height: Float,
        extra: Float = 0.0f,
        mirror: Boolean = false
    ) {
        delegate.setTextureOffset(u, v)
        delegate.addCuboid(x, y, z, length, width, height, extra, mirror)
    }

    inline fun addChild(block: ModelPartBuilder.() -> Unit) {
        +modelPart(model, block)
    }

    operator fun ModelPart.unaryPlus() {
        delegate.addChild(this)
    }
}