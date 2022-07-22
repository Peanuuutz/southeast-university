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

import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sqrt

private const val MINIMUM_SPEED: Double = 1e-3

fun calculateHorizontalSpeed(velocity: Vec3d, yaw: Float): Double { // Note: Result does have sign.
    return with(velocity) {
        val absoluteSpeed = sqrt(x * x + z * z) // Ignore vertical movement
        if (absoluteSpeed > MINIMUM_SPEED) {
            val directionSign = sign(x / absoluteSpeed * fastSinDegree(-yaw) + z / absoluteSpeed * fastCosDegree(yaw))
            absoluteSpeed * directionSign
        } else {
            0.0
        }
    }
}

fun calculateVelocity(horizontalSpeed: Double, verticalSpeed: Double, yaw: Float): Vec3d {
    return if (abs(horizontalSpeed) > MINIMUM_SPEED || abs(verticalSpeed) > MINIMUM_SPEED) {
        Vec3d(horizontalSpeed * fastSinDegree(-yaw), verticalSpeed, horizontalSpeed * fastCosDegree(yaw))
    } else {
        Vec3d.ZERO
    }
}
