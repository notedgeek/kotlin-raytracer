package com.notedgeek.rtace.math

import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.sqrt

var EPSILON = 0.00001

private fun closeTo(a: Double, b: Double) = abs(a - b) < EPSILON

class Tuple internal constructor(val x: Double, val y: Double, val z: Double, val w: Double) {

    init {
        if (w != 1.0 && w != 0.0) {
            throw IllegalArgumentException("tuple cannot be created with w != 1.0 or 0.0 : $w")
        }
    }
    override fun equals(other: Any?) =
        other is Tuple && closeTo(x, other.x) && closeTo(y, other.y) && closeTo (z, other.z) && closeTo(w, other.w)

    operator fun plus(other: Tuple) = Tuple(x + other.x, y + other.y, z + other.z, w + other.w)

    operator fun minus(other: Tuple) = Tuple(x - other.x, y - other.y, z - other.z, w - other.w)

    operator fun unaryMinus() = Tuple(-x, -y, -z, w)

    operator fun times(scalar: Double) = Tuple(x * scalar, y * scalar, z * scalar, w)

    operator fun times(scalar: Int) = times(scalar.toDouble())

    operator fun div(scalar: Double) = Tuple(x / scalar, y / scalar, z / scalar, w)

    operator fun div(scalar: Int) = div(scalar.toDouble())

    infix fun dot(other: Tuple) = x * other.x + y * other.y + z * other.z

    infix fun cross(other: Tuple) = vector(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    fun isPoint() = w == 1.0

    fun isVector() = w == 0.0

    override fun toString(): String {
        return "Tuple(x=$x, y=$y, z=$z, w=$w)"
    }
}

fun tuple(x: Double, y: Double, z: Double, w: Double): Tuple {
    return Tuple(x, y, z, w)
}

fun point(x: Double, y: Double, z: Double) = Tuple(x, y, z, 1.0)

fun vector(x: Double, y: Double, z: Double) = Tuple(x, y, z, 0.0)

fun tuple(x: Int, y: Int, z: Int, w: Int) = tuple(x.toDouble(), y.toDouble(), z.toDouble(), w.toDouble())

fun point(x: Int, y: Int, z: Int) = tuple(x, y, z, 1)

fun vector(x: Int, y: Int, z: Int) = tuple(x, y, z, 0)

fun mag(t: Tuple) = sqrt(t.x * t.x + t.y * t.y + t.z * t.z)

fun norm(t: Tuple) = t / mag(t)
