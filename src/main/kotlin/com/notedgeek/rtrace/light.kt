@file:Suppress("EqualsOrHashCode")

package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.Point

class PointLight(var position: Point, var intensity: Colour) {

    override fun equals(other: Any?) = other is PointLight && position == other.position && intensity == other.intensity

    override fun toString(): String {
        return "PointLight(position=$position, intensity=$intensity)"
    }
}
