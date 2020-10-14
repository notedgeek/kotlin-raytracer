package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.math.PI

class TestMatrices {

    @Nested
    inner class BasicArithmetic {
        @Test
        fun `constructing and inspecting a 4 x 4 matrix`() {
            val m = Matrix(
                1.0, 2.0, 3.0, 4.0,
                5.5, 6.5, 7.5, 8.5,
                9.0, 10.0, 11.0, 12.0,
                13.5, 15.5, 15.5, 16.5
            )
            assertThat(m.get(0, 0)).isEqualTo(1.0)
            assertThat(m.get(0, 3)).isEqualTo(4.0)
            assertThat(m.get(1, 0)).isEqualTo(5.5)
            assertThat(m.get(1, 2)).isEqualTo(7.5)
            assertThat(m.get(2, 2)).isEqualTo(11.0)
            assertThat(m.get(3, 0)).isEqualTo(13.5)
            assertThat(m.get(3, 2)).isEqualTo(15.5)
        }

        @Test
        fun `multiplying two matrices`() {
            val m1 = Matrix(
                1.0, 2.0, 3.0, 4.0,
                5.0, 6.0, 7.0, 8.0,
                9.0, 8.0, 7.0, 6.0,
                5.0, 4.0, 3.0, 2.0
            )
            val m2 = Matrix(
                -2.0, 1.0, 2.0, 3.0,
                3.0, 2.0, 1.0, -1.0,
                4.0, 3.0, 6.0, 5.0,
                1.0, 2.0, 7.0, 8.0
            )
            assertThat(m1 * m2).isEqualTo(
                Matrix(
                    20.0, 22.0, 50.0, 48.0,
                    44.0, 54.0, 114.0, 108.0,
                    40.0, 58.0, 110.0, 102.0,
                    16.0, 26.0, 46.0, 42.0
                )
            )
        }

        @Test
        fun `multiply matrix by point`() {
            val m = Matrix(
                1.0, 2.0, 3.0, 4.0,
                2.0, 4.0, 4.0, 2.0,
                8.0, 6.0, 4.0, 1.0,
                0.0, 0.0, 0.0, 1.0
            )
            val p = Point(1, 2, 3)
            assertThat(m * p).isEqualTo(Point(18, 24, 33))
        }

        @Test
        fun `multiply matrix by vector`() {
            val m = Matrix(
                1.0, 2.0, 3.0, 4.0,
                2.0, 4.0, 4.0, 2.0,
                8.0, 6.0, 4.0, 1.0,
                0.0, 0.0, 0.0, 1.0
            )
            val v = Vector(1, 2, 3)
            assertThat(m * v).isEqualTo(Vector(14, 22, 32))
        }

        @Test
        fun `multiply matrix by the identity matrix`() {
            val m = Matrix(
                0.0, 1.0, 2.0, 4.0,
                1.0, 2.0, 4.0, 8.0,
                2.0, 4.0, 8.0, 16.0,
                4.0, 8.0, 16.0, 32.0
            )
            assertThat(m * I).isEqualTo(m)
            assertThat(I * m).isEqualTo(m)
        }

        @Test
        fun `multiply identity matrix by point`() {
            val p = Point(1.0, -3.5, 6.0)
            assertThat(I * p).isEqualTo(p)
        }

        @Test
        fun `multiply identity matrix by vector`() {
            val v = Vector(1.0, -3.5, 6.0)
            assertThat(I * v).isEqualTo(v)
        }

        @Test
        fun `transposing a matrix`() {
            val m = Matrix(
                0.0, 9.0, 3.0, 0.0,
                9.0, 8.0, 0.0, 8.0,
                1.0, 8.0, 5.0, 3.0,
                0.0, 0.0, 5.0, 8.0
            )
            assertThat(transpose(m)).isEqualTo(
                Matrix(
                    0.0, 9.0, 1.0, 0.0,
                    9.0, 8.0, 8.0, 0.0,
                    3.0, 0.0, 5.0, 5.0,
                    0.0, 8.0, 3.0, 8.0
                )
            )
            assertThat(transpose(transpose(m))).isEqualTo(m)
        }
    }

    @Nested
    inner class Inverse {
        @Test
        fun `determinant of a 2x2 matrix`() {
            val m = Matrix(
                1.0, 5.0,
                -3.0, 2.0
            )
            assertThat(det(m)).isCloseTo(17.0, offset(EPSILON))
        }

        @Test
        fun `sub matrix of a 3x3 matrix is a 2x2 matrix`() {
            val m = Matrix(
                1.0, 5.0, 0.0,
                -3.0, 2.0, 7.0,
                0.0, 6.0, -3.0
            )
            assertThat(subMatrix(m, 0, 2)).isEqualTo(
                Matrix(
                    -3.0, 2.0,
                    0.0, 6.0
                )
            )
        }

        @Test
        fun `sub matrix of a 4x4 matrix is a 3x3 matrix`() {
            val m = Matrix(
                -6.0, 1.0, 1.0, 6.0,
                -8.0, 5.0, 8.0, 6.0,
                -1.0, 0.0, 8.0, 2.0,
                -7.0, 1.0, -1.0, 1.0
            )
            assertThat(subMatrix(m, 2, 1)).isEqualTo(
                Matrix(
                    -6.0, 1.0, 6.0,
                    -8.0, 8.0, 6.0,
                    -7.0, -1.0, 1.0
                )
            )
        }

        @Test
        fun `calculate minor of 3x3 matrix`() {
            val m = Matrix(
                3.0, 5.0, 0.0,
                2.0, -1.0, -7.0,
                6.0, -1.0, 5.0
            )
            val sm = subMatrix(m, 1, 0)
            assertThat(det(sm)).isCloseTo(25.0, offset(EPSILON))
            assertThat(minor(m, 1, 0)).isCloseTo(25.0, offset(EPSILON))
        }

        @Test
        fun `calculate cofactor of 3x3 matrix`() {
            val m = Matrix(
                3.0, 5.0, 0.0,
                2.0, -1.0, -7.0,
                6.0, -1.0, 5.0
            )
            assertThat(minor(m, 0, 0)).isCloseTo(-12.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 0)).isCloseTo(-12.0, offset(EPSILON))
            assertThat(minor(m, 1, 0)).isCloseTo(25.0, offset(EPSILON))
            assertThat(cofactor(m, 1, 0)).isCloseTo(-25.0, offset(EPSILON))
        }

        @Test
        fun `calculate the determinant of a 3x3 matrix`() {
            val m = Matrix(
                1.0, 2.0, 6.0,
                -5.0, 8.0, -4.0,
                2.0, 6.0, 4.0
            )
            assertThat(cofactor(m, 0, 0)).isCloseTo(56.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 1)).isCloseTo(12.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 2)).isCloseTo(-46.0, offset(EPSILON))
            assertThat(det(m)).isCloseTo(-196.0, offset(EPSILON))
        }

        @Test
        fun `calculate the determinant of a 4x4 matrix`() {
            val m = Matrix(
                -2.0, -8.0, 3.0, 5.0,
                -3.0, 1.0, 7.0, 3.0,
                1.0, 2.0, -9.0, 6.0,
                -6.0, 7.0, 7.0, -9.0
            )
            assertThat(cofactor(m, 0, 0)).isCloseTo(690.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 1)).isCloseTo(447.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 2)).isCloseTo(210.0, offset(EPSILON))
            assertThat(cofactor(m, 0, 3)).isCloseTo(51.0, offset(EPSILON))
            assertThat(det(m)).isCloseTo(-4071.0, offset(EPSILON))
        }

        @Test
        fun `calculate the inverse of a 4x4 matrix`() {
            val m = Matrix(
                -5.0, 2.0, 6.0, -8.0,
                1.0, -5.0, 1.0, 8.0,
                7.0, 7.0, -6.0, -7.0,
                1.0, -3.0, 7.0, 4.0
            )
            val mi = -m
            assertThat(det(m)).isCloseTo(532.0, offset(EPSILON))
            assertThat(cofactor(m, 2, 3)).isCloseTo(-160.0, offset(EPSILON))
            assertThat(mi.get(3, 2)).isCloseTo(-160.0 / 532.0, offset(EPSILON))
            assertThat(cofactor(m, 3, 2)).isCloseTo(105.0, offset(EPSILON))
            assertThat(mi.get(2, 3)).isCloseTo(105.0 / 532.0, offset(EPSILON))
            assertThat(mi).isEqualTo(Matrix(
                    0.21805, 0.45113, 0.24060, -0.04511,
                    -0.80827, -1.45677, -0.44361, 0.52068,
                    -0.07895, -0.22368, -0.05263, 0.19737,
                    -0.52256, -0.81391, -0.30075, 0.30639
            ))
        }

        @Test
        fun `calculate inverse of another 4x4 matrix`() {
            val m = Matrix(
                8.0, -5.0, 9.0, 2.0,
                7.0, 5.0, 6.0, 1.0,
                -6.0, 0.0, 9.0, 6.0,
                -3.0, 0.0, -9.0, -4.0
            )
            assertThat(-m).isEqualTo(Matrix(
                    -0.15385, -0.15385, -0.28205, -0.53846,
                    -0.07692, 0.12308, 0.02564, 0.03077,
                    0.35897, 0.35897, 0.43590, 0.92308,
                    -0.69231, -0.69231, -0.76923, -1.92308
            ))
        }

        @Test
        fun `calculate inverse of a third 4x4 matrix`() {
            val m = Matrix(
                9.0, 3.0, 0.0, 9.0,
                -5.0, -2.0, -6.0, -3.0,
                -4.0, 9.0, 6.0, 4.0,
                -7.0, 6.0, 6.0, 2.0
            )
            assertThat(-m).isEqualTo(Matrix(
                -0.04074, -0.07778, 0.14444, -0.22222,
                -0.07778, 0.03333, 0.36667, -0.33333,
                -0.02901, -0.14630, -0.10926, 0.12963,
                0.17778, 0.06667, -0.26667, 0.33333
            ))
        }
    }

    @Nested
    inner class Translation {
        @Test
        fun `multiplying a point by a translation matrix`() {
            val m = translation(1.0, -2.0, 3.0)
            val p = Point(1, 2, 3)
            assertThat(m * p).isEqualTo(Point(2, 0, 6))
        }

        @Test
        fun `multiplying a point by the inverse of a translation matrix`() {
            val m = translation(1.0, -2.0, 3.0)
            val p = Point(1, 2, 3)
            assertThat(-m * p).isEqualTo(Point(0, 4, 0))
        }

        @Test
        fun `translating a vector has no effect`() {
            val m = translation(1.0, -2.0, 3.0)
            val v = Vector(1, 2, 3)
            assertThat(m * v).isEqualTo(v)
        }
    }

    @Nested
    inner class Scaling {
        @Test
        fun `scaling matrix applied to a point`() {
            val s = scaling(2.0, 3.0, 4.0)
            val p = Point(-4, 6, 8)
            assertThat(s * p).isEqualTo(Point(-8, 18, 32))
        }

        @Test
        fun `scaling matrix applied to a vector`() {
            val s = scaling(2.0, 3.0, 4.0)
            val v = Vector(-4, 6, 8)
            assertThat(s * v).isEqualTo(Vector(-8, 18, 32))
        }

        @Test
        fun `multiplying by the inverse of a scaling matrix`() {
            val s = scaling(2.0, 3.0, 4.0)
            val v = Vector(-4, 6, 8)
            assertThat(-s * v).isEqualTo(Vector(-2, 2, 2))
        }
    }

    @Nested
    inner class Rotation {
        @Test
        fun `rotating a point around the x axis`() {
            val p = Point(0, 1, 0)
            val halfQuarter = rotationX(PI / 4)
            val fullQuarter = rotationX(PI / 2)
            assertThat(halfQuarter * p).isEqualTo(Point(0.0, SQ2 / 2, SQ2 / 2))
            assertThat(fullQuarter * p).isEqualTo(Point(0, 0, 1))
        }

        @Test
        fun `rotating a vector around the x axis`() {
            val v = Vector(0, 1, 0)
            val halfQuarter = rotationX(PI / 4)
            val fullQuarter = rotationX(PI / 2)
            assertThat(halfQuarter * v).isEqualTo(Vector(0.0, SQ2 / 2, SQ2 / 2))
            assertThat(fullQuarter * v).isEqualTo(Vector(0, 0, 1))
        }

        @Test
        fun `inverse of an x rotation rotates in the opposite direction`() {
            val p = Point(0, 1, 0)
            val halfQuarter = rotationX(PI / 4)
            assertThat(-halfQuarter * p).isEqualTo(Point(0.0, SQ2 / 2, -SQ2 / 2))

        }

        @Test
        fun `rotating a point around the y axis`() {
            val p = Point(0, 0, 1)
            val halfQuarter = rotationY(PI / 4)
            val fullQuarter = rotationY(PI / 2)
            assertThat(halfQuarter * p).isEqualTo(Point(SQ2 / 2, 0.0, SQ2 / 2))
            assertThat(fullQuarter * p).isEqualTo(Point(1, 0, 0))
        }

        @Test
        fun `rotating a vector around the y axis`() {
            val v = Vector(0, 0, 1)
            val halfQuarter = rotationY(PI / 4)
            val fullQuarter = rotationY(PI / 2)
            assertThat(halfQuarter * v).isEqualTo(Vector(SQ2 / 2, 0.0, SQ2 / 2))
            assertThat(fullQuarter * v).isEqualTo(Vector(1, 0, 0))
        }

        @Test
        fun `inverse of an y rotation rotates in the opposite direction`() {
            val p = Point(0, 0, 1)
            val halfQuarter = rotationY(PI / 4)
            assertThat(-halfQuarter * p).isEqualTo(Point(-SQ2 / 2, 0.0, SQ2 / 2))
        }

        @Test
        fun `rotating a point around the z axis`() {
            val p = Point(0, 1, 0)
            val halfQuarter = rotationZ(PI / 4)
            val fullQuarter = rotationZ(PI / 2)
            assertThat(halfQuarter * p).isEqualTo(Point(-SQ2 / 2, SQ2 / 2, 0.0))
            assertThat(fullQuarter * p).isEqualTo(Point(-1, 0, 0))
        }

        @Test
        fun `rotating a vector around the z axis`() {
            val v = Vector(0, 1, 0)
            val halfQuarter = rotationZ(PI / 4)
            val fullQuarter = rotationZ(PI / 2)
            assertThat(halfQuarter * v).isEqualTo(Vector(-SQ2 / 2, SQ2 / 2, 0.0))
            assertThat(fullQuarter * v).isEqualTo(Vector(-1, 0, 0))
        }

        @Test
        fun `inverse of an z rotation rotates in the opposite direction`() {
            val p = Point(0, 1, 0)
            val halfQuarter = rotationZ(PI / 4)
            assertThat(-halfQuarter * p).isEqualTo(Point(SQ2 / 2, SQ2 / 2, 0.0))
        }
    }

    @Nested
    inner class Shearing {
        private var p = Point(2, 3, 4)

        @Test
        fun `shearing transformation moves x in proportion to y`() {
            val t = shearing(1.0, 0.0, 0.0, 0.0, 0.0, 0.0)
            assertThat(t * p).isEqualTo(Point(5, 3, 4))
        }

        @Test
        fun `shearing transformation moves x in proportion to z`() {
            val t = shearing(0.0, 1.0, 0.0, 0.0, 0.0, 0.0)
            assertThat(t * p).isEqualTo(Point(6, 3, 4))
        }

        @Test
        fun `shearing transformation moves y in proportion to x`() {
            val t = shearing(0.0, 0.0, 1.0, 0.0, 0.0, 0.0)
            assertThat(t * p).isEqualTo(Point(2, 5, 4))
        }

        @Test
        fun `shearing transformation moves y in proportion to z`() {
            val t = shearing(0.0, 0.0, 0.0, 1.0, 0.0, 0.0)
            assertThat(t * p).isEqualTo(Point(2, 7, 4))
        }

        @Test
        fun `shearing transformation moves z in proportion to x`() {
            val t = shearing(0.0, 0.0, 0.0, 0.0, 1.0, 0.0)
            assertThat(t * p).isEqualTo(Point(2, 3, 6))
        }

        @Test
        fun `shearing transformation moves z in proportion to y`() {
            val t = shearing(0.0, 0.0, 0.0, 0.0, 0.0, 1.0)
            assertThat(t * p).isEqualTo(Point(2, 3, 7))
        }
    }

    @Nested
    inner class Chaining {
        private val p = Point(1, 0, 1)
        private val t1 = rotationX(PI / 2)
        private val t2 = scaling(5.0, 5.0, 5.0)
        private val t3 = translation(10.0, 5.0, 7.0)

        @Test
        fun `individual transformations are applied in sequence`() {
            val p1 = t1 * p
            assertThat(p1).isEqualTo(Point(1, -1, 0))
            val p2 = t2 * p1
            assertThat(p2).isEqualTo(Point(5, -5, 0))
            val p3 = t3 * p2
            assertThat(p3).isEqualTo(Point(15, 0, 7))
        }

        @Test
        fun `chained transformations must be applied in reverse order`() {
            val t = t3 * t2 * t1
            assertThat(t * p).isEqualTo(Point(15, 0, 7))
        }
    }
}
