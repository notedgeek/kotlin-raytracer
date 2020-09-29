package com.notedgeek.rtace.math

import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.sqrt

var EPSILON = 0.00001

private fun closeTo(a: Double, b: Double) = abs(a - b) < EPSILON

class Point(val x: Double, val y: Double, val z: Double) {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun minus(other: Point) = Vector(x - other.x, y - other.y, z - other.z)

    operator fun minus(vector: Vector) = Point(x - vector.x, y - vector.y, z - vector.z)

    override fun equals(other: Any?) =
        other is Point && closeTo(x, other.x) && closeTo(y, other.y) && closeTo (z, other.z)
}

class Vector(val x: Double, val y: Double, val z: Double) {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y, z - other.z)

    operator fun times(scalar: Double) = Vector(x * scalar, y * scalar, z * scalar)

    operator fun times(scalar: Int) = times(scalar.toDouble())

    operator fun div(scalar: Double) = Vector(x / scalar, y / scalar, z / scalar)

    operator fun div(scalar: Int) = div(scalar.toDouble())

    operator fun unaryMinus() = Vector(-x, -y, -z)

    fun mag() = sqrt(x * x + y * y + z * z)

    fun norm() = this / mag()

    infix fun dot(other: Vector) = x * other.x + y * other.y + z * other.z

    infix fun cross(other: Vector) = Vector (
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    override fun equals(other: Any?) =
        other is Vector && closeTo(x, other.x) && closeTo(y, other.y) && closeTo (z, other.z)
}

open class Matrix(vararg val elems: Double) {

    private val dim: Int = sqrt(elems.size.toDouble()).toInt()
    private val contents: Array<DoubleArray>

    init {
        if(dim * dim != elems.size) {
            throw IllegalArgumentException("number of elements ${elems.size} not a square number")
        }
        contents = Array(dim) { DoubleArray(dim)}
        var r = 0
        var c = 0
        for (elem in elems) {
            contents[r][c] = elem
            c++
            if(c == dim) {
                c = 0
                r++
            }
        }
    }

    operator fun times(other: Matrix): Matrix {
        if (dim != other.dim) {
            throw IllegalArgumentException("cannot multiply different sized matrices this: $dim other: ${other.dim}")
        }
        val result = DoubleArray(dim * dim)
        var i = 0
        for (r in 0 until dim) {
            for (c in 0 until dim) {
                result[i] = getMultCell(r, c, other)
                i++
            }
        }
        return Matrix(*result)
    }

    private fun getMultCell(r: Int, c: Int, other: Matrix): Double {
        var result = 0.0
        for(i in 0 until dim) {
            result += contents[r][i] * other.contents[i][c]
        }
        return result
    }

    fun get(r: Int, c: Int) =
        if(r !in 0 until dim || c !in 0 until dim) {
            IllegalArgumentException("r or c out of range r: $r c: $c")
        } else {
            contents[r][c]
        }

    override fun equals(other: Any?): Boolean {
        if(other !is Matrix || other.dim != dim) {
            return false
        }
        for(r in 0 until dim) {
            for(c in 0 until dim) {
                if(!closeTo(contents[r][c], other.contents[r][c]))
                    return false
            }
        }
        return true
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for(r in 0 until dim) {
            for(c in 0 until dim) {
                sb.append("${contents[r][c]} ")
            }
            sb.append('\n')
        }
        return sb.toString()
    }
}



