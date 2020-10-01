package com.notedgeek.rtace

class Colour(val red: Double, val green: Double, val blue: Double) {

    operator fun plus(other: Colour) = Colour(red + other.red, green + other.green, blue + other.blue)

    operator fun minus(other: Colour) = Colour(red - other.red, green - other.green, blue - other.blue)

    operator fun times(s: Double) = Colour(s * red, s * green, s * blue)

    operator fun times(other: Colour) = Colour(red * other.red, green * other.green, blue * other.blue)

    override fun equals(other: Any?) = other is Colour &&
            closeTo(red, other.red) && closeTo(green, other.green) && closeTo(blue, other.blue)

    override fun toString(): String {
        return "Colour(red=$red, green=$green, blue=$blue)"
    }
}

fun colour(r: Double, g: Double, b: Double) = Colour(r, g, b)
