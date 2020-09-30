package com.notedgeek.rtrace

import com.notedgeek.rtace.*
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
            val v1 = vector(3, -2, 5)
            val v2 = vector(-2, 3, 1)
            assertThat(v1 + v2).isEqualTo(vector(1, 1, 6))
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
    }

    @Nested
    inner class ScalarMultiplicationAndDivision{
        @Test
        fun `multiplying a vector by an int scalar`() {
            val v = vector(1, -2, 3)
            assertThat(v * 2).isEqualTo(vector(2, -4, 6))
        }

        @Test
        fun `multiplying a vector by a double scalar`() {
            val v = vector(2, -2, 4)
            assertThat(v * 0.5).isEqualTo(vector(1, -1, 2))
        }

        @Test
        fun `dividing a vector by an int scalar`() {
            val v = vector(2, -2, 4)
            assertThat(v / 2).isEqualTo(vector(1, -1, 2))
        }

        @Test
        fun `dividing a vector by a double scalar`() {
            val v = vector(2, -2, 4)
            assertThat(v / 1.5).isEqualTo(vector(1.33333, -1.33333, 2.66666))
        }
    }

    @Nested
    inner class MagnitudeAndNormalisation {
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
        fun `normalising vector(4, 0, 0) gives (1, 0, 0)`() {
            val v = vector(4, 0, 0)
            assertThat(normalise(v)).isEqualTo(
                vector(
                    1,
                    0,
                    0
                )
            )
        }

        @Test
        fun `normalising vector(1, 2, 3)`() {
            val v = vector(1, 2, 3)
            val s14 = sqrt(14.0)
            assertThat(normalise(v)).isEqualTo(
                vector(
                    1.0 / s14,
                    2.0 / s14,
                    3.0 / s14
                )
            )
        }
    }

    @Nested
    inner class DotAndCrossProducts {
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
}