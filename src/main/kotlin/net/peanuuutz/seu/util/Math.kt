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

@file:Suppress("NOTHING_TO_INLINE")

package net.peanuuutz.seu.util

import net.minecraft.util.math.MathHelper
import kotlin.math.PI

const val PI: Float = PI.toFloat()

const val TAU: Float = net.peanuuutz.seu.util.PI * 2

inline fun Float.toRadian(): Float = toDouble().let(Math::toRadians).toFloat()

inline fun fastSin(x: Float): Float = MathHelper.sin(x)

inline fun fastSinDegree(degree: Float): Float = fastSin(degree.toRadian())

inline fun fastCos(x: Float): Float = MathHelper.cos(x)

inline fun fastCosDegree(degree: Float): Float = fastCos(degree.toRadian())