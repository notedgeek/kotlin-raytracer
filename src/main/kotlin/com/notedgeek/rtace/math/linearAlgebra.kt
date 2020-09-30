package com.notedgeek.rtace.math

import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

var EPSILON = 0.00001

var I = Matrix(
    1.0, 0.0, 0.0, 0.0,
    0.0, 1.0, 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
)

private fun closeTo(a: Double, b: Double) = abs(a - b) < EPSILON

class Point(val x: Double, val y: Double, val z: Double) {

    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun minus(other: Point) = Vector(x - other.x, y - other.y, z - other.z)

    operator fun minus(vector: Vector) = Point(x - vector.x, y - vector.y, z - vector.z)

    override fun equals(other: Any?) =
        other is Point && closeTo(x, other.x) && closeTo(y, other.y) && closeTo (z, other.z)

    override fun toString(): String {
        return "Point(x=$x, y=$y, z=$z)"
    }
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

    fun normalise() = this / mag()

    infix fun dot(other: Vector) = x * other.x + y * other.y + z * other.z

    infix fun cross(other: Vector) = Vector (
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    override fun equals(other: Any?) =
        other is Vector && closeTo(x, other.x) && closeTo(y, other.y) && closeTo (z, other.z)

    override fun toString(): String {
        return "Vector(x=$x, y=$y, z=$z)"
    }
}

open class Matrix(vararg elems: Double) {

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
        val destArray = DoubleArray(dim * dim)
        for (r in 0 until dim) {
            for (c in 0 until dim) {
                destArray[r * dim + c] = getMultCell(r, c, other)
            }
        }
        return Matrix(*destArray)
    }

    operator fun times(p: Point) : Point {
        val tuple = times(p.x, p.y, p.z, 1.0)
        return Point(tuple[0], tuple[1], tuple[2])
    }

    operator fun times(v: Vector) : Vector {
        val tuple = times(v.x, v.y, v.z, 0.0)
        return Vector(tuple[0], tuple[1], tuple[2])
    }

    operator fun unaryMinus(): Matrix {
        val det = det()
        if (closeTo(det, 0.0)) {
            throw IllegalArgumentException("un-invertible matrix $this")
        }
        val destArray = DoubleArray(dim * dim)
        for (r in 0 until dim) {
            for (c in 0 until dim) {
                destArray[c * dim + r] = cofactor(r, c) / det
            }
        }
        return Matrix(*destArray)
    }

    fun transpose(): Matrix {
        val destArray = DoubleArray(dim * dim)
        for (c in 0 until dim) {
            for (r in 0 until dim) {
                destArray[c * dim + r] = contents[r][c]
            }
        }
        return Matrix(*destArray)
    }

    fun det(): Double =
        if (dim == 2) {
            contents[0][0] * contents [1][1] - contents[0][1] * contents[1][0]
        } else {
            var det = 0.0
            for (i in 0 until dim) {
                det += contents[0][i] * cofactor(0, i)
            }
            det
        }

    fun submatrix(rSkip: Int, cSkip: Int): Matrix {
        if (dim < 2) {
            throw IllegalArgumentException("matrix of dimension: $dim, too small to take sub matrix")
        }
        if (rSkip !in 0 until dim || cSkip !in 0 until dim) {
            throw IllegalArgumentException("r or c out of range r: $rSkip c: $cSkip")
        }
        val destArray = DoubleArray((dim - 1) * (dim -1))
        var i = 0
        for (r in 0 until dim) {
            if (r != rSkip) {
                for (c in 0 until dim) {
                    if(c != cSkip) {
                        destArray[i] = contents[r][c]
                        i++
                    }
                }
            }
        }
        return Matrix(*destArray)
    }

    fun minor(r: Int, c: Int): Double {
        if (dim < 3) {
            throw IllegalArgumentException("dim must be 3 or higher, was: $dim")
        }
        return submatrix(r, c).det()
    }

    fun cofactor(r: Int, c: Int): Double = if ((r + c) % 2 == 1) -minor(r, c) else minor(r, c)

    private fun times(x: Double, y: Double, z: Double, w: Double): DoubleArray {
        if(dim != 4) {
            throw IllegalArgumentException("matrix to multiply should be of dimension 4: $dim")
        }
        val result = DoubleArray(4)
        val tuple =  doubleArrayOf(x, y, z, w)
        for(i in 0 until dim) {
            result[i] = getTupleMultCell(i, tuple)
        }
        return result
    }

    fun get(r: Int, c: Int): Double {
        if (r !in 0 until dim || c !in 0 until dim) {
            throw IllegalArgumentException("r or c out of range r: $r c: $c")
        } else {
            return contents[r][c]
        }
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

    private fun getMultCell(r: Int, c: Int, other: Matrix): Double {
        var result = 0.0
        for(i in 0 until dim) {
            result += contents[r][i] * other.contents[i][c]
        }
        return result
    }

    private fun getTupleMultCell(r: Int, tuple: DoubleArray): Double {
        var result = 0.0
        for(i in 0 until dim) {
            result += contents[r][i] * tuple[i]
        }
        return result
    }
}

fun vector(x: Double, y: Double, z: Double) = Vector(x, y, z)

fun vector(x: Int, y: Int, z: Int) = Vector(x, y, z)

fun point(x: Double, y: Double, z: Double) = Point(x, y, z)

fun point(x: Int, y: Int, z: Int) = Point(x, y, z)

fun matrix(vararg elems: Double) = Matrix(*elems)

fun mag(v: Vector) =  v.mag()

fun normalise(v: Vector) = v.normalise()

fun transpose(m: Matrix) = m.transpose()

fun det(m: Matrix) = m.det()

fun submatrix(m: Matrix, r: Int, c: Int) = m.submatrix(r, c)

fun minor(m: Matrix, r: Int, c: Int) = m.minor(r, c)

fun cofactor(m: Matrix, r: Int, c: Int) = m.cofactor(r, c)

fun translation(x: Double, y: Double, z: Double) = Matrix(
    1.0, 0.0, 0.0, x,
    0.0, 1.0, 0.0, y,
    0.0, 0.0, 1.0, z,
    0.0, 0.0, 0.0, 1.0
)

fun scaling(x: Double, y: Double, z: Double) = Matrix(
    x, 0.0, 0.0, 0.0,
    0.0, y, 0.0, 0.0,
    0.0, 0.0, z, 0.0,
    0.0, 0.0, 0.0, 1.0
)

fun rotationX(r: Double) = Matrix(
    1.0, 0.0,     0.0,    0.0,
    0.0, cos(r), -sin(r), 0.0,
    0.0, sin(r),  cos(r), 0.0,
    0.0, 0.0,     0.0,    1.0
)

fun rotationY(r: Double) = Matrix(
    cos(r),  0.0, sin(r), 0.0,
    0.0,     1.0, 0.0,    0.0,
    -sin(r), 0.0, cos(r), 0.0,
    0.0,     0.0, 0.0,    1.0
)

fun rotationZ(r: Double) = Matrix(
    cos(r), -sin(r), 0.0, 0.0,
    sin(r),  cos(r), 0.0, 0.0,
    0.0,        0.0, 1.0, 0.0,
    0.0,        0.0, 0.0, 1.0
)

fun shearing(xy: Double, xz: Double, yx: Double, yz: Double, zx: Double, zy: Double) = Matrix(
    1.0, xy, xz,  0.0,
    yx, 1.0, yz,  0.0,
    zx, zy, 1.0,  0.0,
    0.0, 0.0, 0.0, 1.0
)