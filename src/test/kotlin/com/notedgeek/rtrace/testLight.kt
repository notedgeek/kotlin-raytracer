package com.notedgeek.rtrace

import com.notedgeek.rtace.Colour
import com.notedgeek.rtace.Point
import com.notedgeek.rtace.PointLight
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestLight {

    @Test
    fun `a Point light has position and intensity`() {
        val position = Point(0, 0, 0)
        val intensity = Colour(1.0, 1.0, 1.0)
        val light = PointLight(position, intensity)
        assertThat(light.position).isEqualTo(position)
        assertThat(light.intensity).isEqualTo(intensity)
    }
}