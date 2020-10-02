package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

val material = material()
val position = point(0, 0, 0)

class TestLighting {

    @Test
    fun `lighting with eye between the light and the surface`() {
        val eyeV = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 0, -10), WHITE)
        val result = lighting(material, light, position, eyeV, normal)
        Assertions.assertThat(result).isEqualTo(colour(1.9, 1.9, 1.9))
    }

    @Test
    fun `lighting with eye between the light and the surface, eye offset 45`() {
        val eyeV = vector(0.0, SQ2 / 2, SQ2 / 2)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 0, -10), WHITE)
        val result = lighting(material, light, position, eyeV, normal)
        Assertions.assertThat(result).isEqualTo(colour(1.0, 1.0, 1.0))
    }

    @Test
    fun `lighting with eye opposite surface surface, light offset 45`() {
        val eyeV = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 10, -10), WHITE)
        val result = lighting(material, light, position, eyeV, normal)
        Assertions.assertThat(result).isEqualTo(colour(0.7364, 0.7364, 0.7364))
    }

    @Test
    fun `lighting with eye in the path of reflection vector`() {
        val eyeV = vector(0.0, -SQ2 / 2, -SQ2 / 2)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 10, -10), WHITE)
        val result = lighting(material, light, position, eyeV, normal)
        Assertions.assertThat(result).isEqualTo(colour(1.6364, 1.6364, 1.6364))
    }

    @Test
    fun `lighting with the light behind surface`() {
        val eyeV = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 0, 10), WHITE)
        val result = lighting(material, light, position, eyeV, normal)
        Assertions.assertThat(result).isEqualTo(colour(0.1, 0.1, 0.1))
    }

    @Test
    fun `lighting with the surface in shadow`() {
        val eyeV = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(point(0, 0, -10), WHITE)
        val inShadow = true
        val result = lighting(material, light, position, eyeV, normal, inShadow)
        Assertions.assertThat(result).isEqualTo(colour(0.1, 0.1, 0.1))
    }
}