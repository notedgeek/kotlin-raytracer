package com.notedgeek.rtrace.math

import com.notedgeek.rtace.math.Matrix
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MatrixTest {

    @Test
    fun `constructing and inspecting a 4 x 4 matrix` () {
        val m = Matrix(
            1.0, 2.0, 3.0, 4.0,
            5.5, 6.5, 7.5, 8.5,
            9.0, 10.0, 11.0, 12.0,
            13.5, 15.5, 15.5, 16.5
        )
        assertThat(m.get(0,0)).isEqualTo(1.0)
        assertThat(m.get(0,3)).isEqualTo(4.0)
        assertThat(m.get(1,0)).isEqualTo(5.5)
        assertThat(m.get(1,2)).isEqualTo(7.5)
        assertThat(m.get(2,2)).isEqualTo(11.0)
        assertThat(m.get(3,0)).isEqualTo(13.5)
        assertThat(m.get(3,2)).isEqualTo(15.5)
    }

    @Test
    fun `multiplying two matrices` () {
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
        assertThat(m1 * m2).isEqualTo(Matrix(
            20.0, 22.0, 50.0, 48.0,
            44.0, 54.0, 114.0, 108.0,
            40.0, 58.0, 110.0, 102.0,
            16.0, 26.0, 46.0, 42.0
        ))
    }

}
