package com.notedgeek.rtrace.maths

import java.lang.IllegalArgumentException
import kotlin.math.*

private class Polynomial(vararg val data: Double) {

    val order: Int
        get() = data.size

    fun padRTo(size: Int): Polynomial {
        return if (size <= order) {
            this
        } else {
            val array = DoubleArray(size)
            for (i in 0 until size) {
                if (i < order) {
                    array[i] = data[i]
                }
            }
            Polynomial(*array)
        }
    }

    fun padLTo(size: Int): Polynomial {
        return if (size <= order) {
            this
        } else {
            val array = DoubleArray(size)
            val leadingZeroCount = size - order
            for (i in 0 until size) {
                if (i >= leadingZeroCount) {
                    array[i] = data[i - leadingZeroCount]
                }
            }
            Polynomial(*array)
        }
    }

    operator fun plus(other: Polynomial): Polynomial {
        val size = max(this.order, other.order)
        val thisP = this.padRTo(size)
        val otherP = other.padRTo(size)
        val array = DoubleArray(size)
        for (i in 0 until size) {
            array[i] = thisP.data[i] + otherP.data[i]
        }
        return Polynomial(*array)
    }

    operator fun times(scalar: Double): Polynomial {
        val array = DoubleArray(order)
        for (i in 0 until order) {
            array[i] = data[i] * scalar
        }
        return Polynomial(*array)
    }

    operator fun times(other: Polynomial): Polynomial {
        var result = Polynomial(0.0)
        for ((power, i) in (0 until other.order).withIndex()) {
            val subResult = this.padLTo(order + power) * other.data[i]
            result += subResult
        }
        return result
    }

    override fun toString(): String {
        return "Polynomial(data=${data.contentToString()})"
    }
}

private fun withRoots(vararg roots: Double): Polynomial {
    var result = Polynomial(1.0)
    for (root in roots) {
        result *= Polynomial(-root, 1.0)
    }
    return result
}

/*
 *  Converted from https://github.com/marcin-chwedczuk/ray_tracing_torus_js/blob/master/app/scripts/solver.js
 *
 *  with thanks - original comments below
 *
 *  Roots3And4.c
 *
 *  Utility functions to find cubic and quartic roots,
 *  coefficients are passed like this:
 *
 *      c[0] + c[1]*x + c[2]*x^2 + c[3]*x^3 + c[4]*x^4 = 0
 *
 *  The functions return the number of non-complex roots and
 *  put the values into the s array.
 *
 *  Author:         Jochen Schwarze (schwarze@isa.de)
 *
 *  Jan 26, 1990    Version for Graphics Gems
 *  Oct 11, 1990    Fixed sign problem for negative q's in solve4
 *  	    	    (reported by Mark Podlipec),
 *  	    	    Old-style function definitions,
 *  	    	    isZero() as a macro
 *  Nov 23, 1990    Some systems do not declare acos() and Math.cbrt() in
 *                  <math.h>, though the functions exist in the library.
 *                  If large coefficients are used, EQN_EPS should be
 *                  reduced considerably (e.g. to 1E-30), results will be
 *                  correct but multiple roots might be reported more
 *                  than once.
 */
private const val EQN_EPS = 1e-64
private fun isZero(x: Double) = abs(x) < EQN_EPS

private fun DoubleArray.push(d: Double): DoubleArray {
    val result = DoubleArray(this.size + 1)
    for (i in result.indices) {
        if(i == this.size) {
            result[i] = d
        } else {
            result[i] = this[i]
        }
    }
    return result
}

private fun DoubleArray.concat(other: DoubleArray): DoubleArray {
    val result = DoubleArray(this.size + other.size)
    for (i in result.indices) {
        if(i >= this.size) {
            result[i] = other[i - this.size]
        } else {
            result[i] = this[i]
        }
    }
    return result
}

fun solve2(c: DoubleArray): DoubleArray {
    if (c.size != 3) {
        throw IllegalArgumentException("order must be 2 - ${c.size - 1}")
    }
    val p = c[1] / (2 * c[2])
    val q = c[0] / c[2]

    val d = p * p - q

    return when {
        isZero(d) -> {
            doubleArrayOf(-p)
        }
        d < 0 -> {
            doubleArrayOf()
        }
        else -> {
            val sqrtD = sqrt(d)
            doubleArrayOf(sqrtD - p, -sqrtD - p)
        }
    }
}

fun solve3(c: DoubleArray): DoubleArray {
    if (c.size != 4) {
        throw IllegalArgumentException("order must be 3 - ${c.size - 1}")
    }
    val aA = c[2] / c[3]
    val bB = c[1] / c[3]
    val cC = c[0] / c[3]
    val sqA = aA * aA
    val p = 1.0 / 3 * (-1.0 / 3 * sqA + bB)
    val q = 1.0 / 2 * (2.0 / 27 * aA * sqA - 1.0 / 3 * aA * bB + cC)

    val cbP = p * p * p
    val d = q * q + cbP

    val s: DoubleArray?

    when {
        isZero(d) -> {
            s = if (isZero(q)) {
                doubleArrayOf(0.0)
            } else {
                val u = Math.cbrt(-q)
                doubleArrayOf(2 * u, -u)
            }
        }
        d < 0 -> {
            val phi = 1.0 / 3 * acos(-q / sqrt(-cbP))
            val t= 2 * sqrt(-p)
            s = doubleArrayOf(t * cos(phi), -t * cos(phi + PI / 3), -t * cos(phi - PI / 3))
        }
        else -> {
            val sqrtD = sqrt(d)
            val u = Math.cbrt(sqrtD - q)
            val v = - Math.cbrt(sqrtD + q)
            s = doubleArrayOf(u + v)
        }
    }

    val sub = 1.0 / 3 * aA

    for(i in s.indices) {
        s[i] -= sub
    }

    return s
}

fun solve4(c: DoubleArray): DoubleArray {
    if (c.size != 5) {
        throw IllegalArgumentException("order must be 4 - ${c.size - 1}")
    }
    val aA = c[3] / c[4]
    val bB = c[2] / c[4]
    val cC = c[1] / c[4]
    val dD = c[0] / c[4]

    val sqA = aA * aA
    val p = -3.0 / 8 * sqA + bB
    val q = 1.0 / 8 * sqA * aA - 1.0 / 2 * aA * bB + cC
    val r = -3.0 / 256 * sqA * sqA + 1.0 / 16 * sqA * bB - 1.0 / 4 * aA * cC + dD

    var s: DoubleArray?

    if(isZero(r)) {
        val coeffs = doubleArrayOf(q, p, 0.0, 1.0)
        s = solve3(coeffs).push(0.0)
    } else {
        var coeffs = doubleArrayOf(
                1.0 / 2 * r * p - 1.0 / 8 * q * q,
                -r,
                -1.0 / 2 * p,
                1.0
        )

        s = solve3(coeffs)

        val z = s[0]
        var u = z * z - r
        var v = 2 * z - p

        u = when {
            isZero(u) -> {
                0.0
            }
            u > 0 -> {
                sqrt(u)
            }
            else -> {
                return doubleArrayOf(0.0)
            }
        }

        v = when {
            isZero(v) -> {
                0.0
            }
            v > 0 -> {
                sqrt(v)
            }
            else -> {
                return doubleArrayOf(0.0)
            }
        }

        coeffs = doubleArrayOf(z - u, if (q < 0.0) -v else v, 1.0)

        s = solve2(coeffs)

        coeffs = doubleArrayOf(z + u, if (q < 0.0) v else -v, 1.0)

        s = s.concat(solve2(coeffs))
    }

    val sub = 1.0 / 4 * aA

    for(i in s.indices) {
        s[i] -= sub
    }

    return s
}

fun main() {
    val pol3 = withRoots(3.25, -33.24, 7.23)
    println(pol3)
    println(Polynomial(*solve3(pol3.data)))
    val pol4 = withRoots(5.0, 5.0, 5.0, 1.0)
    println(pol4)
    println(Polynomial(*solve4(pol4.data)))
}