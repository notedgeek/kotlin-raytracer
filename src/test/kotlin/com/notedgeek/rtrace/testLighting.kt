package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.SQ2
import com.notedgeek.rtrace.maths.Vector
import com.notedgeek.rtrace.obj.Sphere
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

private val material = Material()
private val position = Point(0, 0, 0)
private val dummyObj = Sphere()

class TestLighting {

    @Test
    fun `lighting with eye between the light and the surface`() {
        val eyeV = Vector(0, 0, -1)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, -10), WHITE)
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj)
        Assertions.assertThat(result).isEqualTo(Colour(1.9, 1.9, 1.9))
    }

    @Test
    fun `lighting with eye between the light and the surface, eye offset 45`() {
        val eyeV = Vector(0.0, SQ2 / 2, SQ2 / 2)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, -10), WHITE)
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj)
        Assertions.assertThat(result).isEqualTo(Colour(1.0, 1.0, 1.0))
    }

    @Test
    fun `lighting with eye opposite surface surface, light offset 45`() {
        val eyeV = Vector(0, 0, -1)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 10, -10), WHITE)
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj)
        Assertions.assertThat(result).isEqualTo(Colour(0.7364, 0.7364, 0.7364))
    }

    @Test
    fun `lighting with eye in the path of reflection vector`() {
        val eyeV = Vector(0.0, -SQ2 / 2, -SQ2 / 2)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 10, -10), WHITE)
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj)
        Assertions.assertThat(result).isEqualTo(Colour(1.6364, 1.6364, 1.6364))
    }

    @Test
    fun `lighting with the light behind surface`() {
        val eyeV = Vector(0, 0, -1)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, 10), WHITE)
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj)
        Assertions.assertThat(result).isEqualTo(Colour(0.1, 0.1, 0.1))
    }

    @Test
    fun `lighting with the surface in shadow`() {
        val eyeV = Vector(0, 0, -1)
        val normal = Vector(0, 0, -1)
        val light = PointLight(Point(0, 0, -10), WHITE)
        val inShadow = true
        val result = surfaceColour(material, light, position, eyeV, normal, dummyObj, inShadow)
        Assertions.assertThat(result).isEqualTo(Colour(0.1, 0.1, 0.1))
    }
}