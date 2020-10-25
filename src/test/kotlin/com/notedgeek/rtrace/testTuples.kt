package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class TestTuples {

    @Nested
    inner class AdditionAndSubtraction {
        @Test
        fun `adding two vectors`() {
            val v1 = Vector(3, -2, 5)
            val v2 = Vector(-2, 3, 1)
            assertThat(v1 + v2).isEqualTo(Vector(1, 1, 6))
        }

        @Test
        fun `subtracting two points produces a vector`() {
            val p1 = Point(3, 2, 1)
            val p2 = Point(5, 6, 7)
            assertThat(p1 - p2).isEqualTo(Vector(-2, -4, -6))
        }

        @Test
        fun `adding a vector to a point produces a point`() {
            val p = Point(3, 2, 1)
            val v = Vector(5, 6, 7)
            assertThat(p + v).isEqualTo(Point(8, 8, 8))
        }

        @Test
        fun `subtracting a vector from a point produces a point`() {
            val p = Point(3, 2, 1)
            val v = Vector(5, 6, 7)
            assertThat(p - v).isEqualTo(Point(-2, -4, -6))
        }

        @Test
        fun `subtracting two vectors produces a vector`() {
            val v1 = Vector(3, 2, 1)
            val v2 = Vector(5, 6, 7)
            assertThat(v1 - v2).isEqualTo(Vector(-2, -4, -6))
        }
    }

    @Nested
    inner class ScalarMultiplicationAndDivision{
        @Test
        fun `multiplying a vector by an int scalar`() {
            val v = Vector(1, -2, 3)
            assertThat(v * 2).isEqualTo(Vector(2, -4, 6))
        }

        @Test
        fun `multiplying a vector by a double scalar`() {
            val v = Vector(2, -2, 4)
            assertThat(v * 0.5).isEqualTo(Vector(1, -1, 2))
        }

        @Test
        fun `dividing a vector by an int scalar`() {
            val v = Vector(2, -2, 4)
            assertThat(v / 2).isEqualTo(Vector(1, -1, 2))
        }

        @Test
        fun `dividing a vector by a double scalar`() {
            val v = Vector(2, -2, 4)
            assertThat(v / 1.5).isEqualTo(Vector(1.33333, -1.33333, 2.66666))
        }
    }

    @Nested
    inner class MagnitudeAndNormalisation {
        @Test
        fun `computing the magnitude of vector (1, 0, 0)`() {
            val v = Vector(1, 0, 0)
            assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
        }

        @Test
        fun `computing the magnitude of Vector (0, 1, 0)`() {
            val v = Vector(0, 1, 0)
            assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
        }

        @Test
        fun `computing the magnitude of vector (0, 0, 1)`() {
            val v = Vector(0, 0, 1)
            assertThat(mag(v)).isCloseTo(1.0, offset(EPSILON))
        }

        @Test
        fun `computing the magnitude of vector (1, 2, 3)`() {
            val v = Vector(1, 2, 3)
            assertThat(mag(v)).isCloseTo(sqrt(14.0), offset(EPSILON))
        }

        @Test
        fun `computing the magnitude of vector (-1, -2, -3)`() {
            val v = Vector(-1, -2, -3)
            assertThat(mag(v)).isCloseTo(sqrt(14.0), offset(EPSILON))
        }

        @Test
        fun `normalising vector(4, 0, 0) gives (1, 0, 0)`() {
            val v = Vector(4, 0, 0)
            assertThat(normalise(v)).isEqualTo(Vector(1, 0, 0))
        }

        @Test
        fun `normalising Vector(1, 2, 3)`() {
            val v = Vector(1, 2, 3)
            val s14 = sqrt(14.0)
            assertThat(normalise(v)).isEqualTo(Vector(1.0 / s14, 2.0 / s14, 3.0 / s14))
        }
    }

    @Nested
    inner class DotAndCrossProducts {
        @Test
        fun `dot product of two Vectors`() {
            val v1 = Vector(1, 2, 3)
            val v2 = Vector(2, 3, 4)
            assertThat(v1 dot v2).isCloseTo(20.0, offset(EPSILON))
        }

        @Test
        fun `cross product of two vectors`() {
            val v1 = Vector(1, 2, 3)
            val v2 = Vector(2, 3, 4)
            assertThat(v1 cross v2).isEqualTo(Vector(-1, 2, -1))
            assertThat(v2 cross v1).isEqualTo(Vector(1, -2, 1))
        }
    }
}