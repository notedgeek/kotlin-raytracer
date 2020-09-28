package com.notedgeek.rtrace.math

import com.notedgeek.rtace.math.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.sqrt

class TuplesTest {

    @Test
    fun `cannot create a tuple with w != 1 or 0 `() {
        assertThrows<IllegalArgumentException> {
            tuple(4.3, -4.2, 3.1, 2.0)
        }
    }

    @Test
    fun `a tuple with w = 1 is a point`() {
        val t = tuple(4.3, -4.2, 3.1, 1.0)
        assertThat(t.x).isEqualTo(4.3)
        assertThat(t.y).isEqualTo(-4.2)
        assertThat(t.z).isEqualTo(3.1)
        assertThat(t.w).isEqualTo(1.0)
        assert(t.isPoint())
        assert(!t.isVector())
    }

    @Test
    fun `a tuple with w = 0 is a vector`() {
        val t = tuple(4.3, -4.2, 3.1, 0.0)
        assertThat(t.x).isEqualTo(4.3)
        assertThat(t.y).isEqualTo(-4.2)
        assertThat(t.z).isEqualTo(3.1)
        assertThat(t.w).isEqualTo(0.0)
        assert(t.isVector())
        assert(!t.isPoint())
    }

    @Test
    fun `point() creates tuples with w = 1`() {
        val p = point(4, -4, 3)
        assertThat(p).isEqualTo(tuple(4, -4, 3, 1))
    }

    @Test
    fun `vector() creates tuples with w = 0`() {
        val v = vector(4, -4, 3)
        assertThat(v).isEqualTo(tuple(4, -4, 3, 0))
    }

    @Test
    fun `adding two tuples`() {
        val t1 = tuple(3, -2, 5, 1)
        val t2 = tuple(-2, 3, 1, 0)
        assertThat(t1 + t2).isEqualTo(tuple(1, 1, 6, 1))
    }

    @Test
    fun `subtracting two points produces a vector`() {
        val p1 = point(3, 2, 1)
        val p2 = point(5, 6, 7)
        assertThat(p1 - p2).isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `subtracting a vector from a point produces a point`() {
        val p = point(3, 2, 1)
        val v = vector(5, 6, 7)
        assertThat(p - v).isEqualTo(point(-2, -4, -6))
    }

    @Test
    fun `subtracting two vectors produces a vector`() {
        val v1 = vector(3, 2, 1)
        val v2 = vector(5, 6, 7)
        assertThat(v1 - v2).isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `multiplying a tuple by an int scalar`() {
        val t = tuple(1, -2, 3, 1)
        assertThat(t * 2).isEqualTo(tuple(2, -4, 6, 1))
    }

    @Test
    fun `multiplying a tuple by an double scalar`() {
        val t = tuple(2, -2, 4, 1)
        assertThat(t * 0.5).isEqualTo(tuple(1, -1, 2, 1))
    }

    @Test
    fun `dividing a tuple by an int scalar`() {
        val t = tuple(2, -2, 4, 1)
        assertThat(t / 2).isEqualTo(tuple(1, -1, 2, 1))
    }

    @Test
    fun `dividing a tuple by an double scalar`() {
        val t = tuple(2, -2, 4, 1)
        assertThat(t / 1.5).isEqualTo(tuple(1.33333, -1.33333, 2.66666, 1.0))
    }

    @Test
    fun `computing the magnitude of vector (1, 0, 0)`() {
        val v = vector(1, 0, 0)
        assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
    }

    @Test
    fun `computing the magnitude of vector (0, 1, 0)`() {
        val v = vector(0, 1, 0)
        assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
    }

    @Test
    fun `computing the magnitude of vector (0, 0, 1)`() {
        val v = vector(0, 0, 1)
        assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
    }

    @Test
    fun `computing the magnitude of vector (1, 2, 3)`() {
        val v = vector(1, 2, 3)
        assertThat(mag(v)).isCloseTo(sqrt(14.0), offset(EPSILON))
    }

    @Test
    fun `computing the magnitude of vector (-1, -2, -3)`() {
        val v = vector(-1, -2, -3)
        assertThat(mag(v)).isCloseTo(sqrt(14.0), offset(EPSILON))
    }

    @Test
    fun `normalising vector(4, 0, 0) gives (1, 0, 0)` () {
        val v = vector(4, 0, 0)
        assertThat(norm(v)).isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `normalising vector(1, 2, 3)` () {
        val v = vector(1, 2, 3)
        val s14 = sqrt(14.0)
        assertThat(norm(v)).isEqualTo(vector(1.0 / s14, 2.0 / s14, 3.0 / s14))
    }

    @Test
    fun `dot product of two vectors`() {
        val v1 = vector(1, 2, 3)
        val v2 = vector(2, 3, 4)
        assertThat(v1 dot v2).isCloseTo(20.0, offset(EPSILON))
    }

    @Test
    fun `cross product of two vectors`() {
        val v1 = vector(1, 2, 3)
        val v2 = vector(2, 3, 4)
        assertThat(v1 cross v2).isEqualTo(vector(-1, 2, -1))
        assertThat(v2 cross v1).isEqualTo(vector(1, -2, 1))
    }
}