package com.notedgeek.rtace

import java.awt.Color
import kotlin.math.min

val BLACK = Colour(0.0, 0.0, 0.0)
val WHITE = Colour(1.0, 1.0, 1.0)

class Colour(val red: Double, val green: Double, val blue: Double) {

    operator fun plus(other: Colour) = Colour(red + other.red, green + other.green, blue + other.blue)

    operator fun minus(other: Colour) = Colour(red - other.red, green - other.green, blue - other.blue)

    operator fun times(s: Double) = Colour(s * red, s * green, s * blue)

    operator fun times(other: Colour) = Colour(red * other.red, green * other.green, blue * other.blue)

    fun toAWT() = Color(min(255, (red * 255).toInt()), min(255, (green * 255).toInt()), min(255, (blue * 255).toInt()))

    override fun equals(other: Any?) = other is Colour &&
            closeTo(red, other.red) && closeTo(green, other.green) && closeTo(blue, other.blue)

    override fun toString(): String {
        return "Colour(red=$red, green=$green, blue=$blue)"
    }
}
