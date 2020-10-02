package com.notedgeek.rtrace

import com.notedgeek.rtace.colour
import com.notedgeek.rtace.point
import com.notedgeek.rtace.pointLight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestLight {

    @Test
    fun `a point light has position and intensity`() {
        val position = point(0, 0, 0)
        val intensity = colour(1.0, 1.0, 1.0)
        val light = pointLight(position, intensity)
        assertThat(light.position).isEqualTo(position)
        assertThat(light.intensity).isEqualTo(intensity)
    }
}