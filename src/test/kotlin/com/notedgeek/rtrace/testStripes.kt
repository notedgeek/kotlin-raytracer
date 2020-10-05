package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.Sphere
import com.notedgeek.rtace.pattern.Stripes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private val dummyObj = Sphere()

class TestStripes {

    @Test
    fun `creating a stripe pattern`() {
        val pattern = Stripes(WHITE, BLACK)
        assertThat(pattern.colour1).isEqualTo(WHITE)
        assertThat(pattern.colour2).isEqualTo(BLACK)
    }

    @Test
    fun `a stripe pattern is constant in y`() {
        val pattern = Stripes(WHITE, BLACK)
        assertThat(pattern.colourAt(Point(0, 0, 0))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(0, 1, 0))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(0, 2, 0))).isEqualTo(WHITE)
    }

    @Test
    fun `a stripe pattern is constant in z`() {
        val pattern = Stripes(WHITE, BLACK)
        assertThat(pattern.colourAt(Point(0, 0, 0))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(0, 0, 1))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(0, 0, 2))).isEqualTo(WHITE)
    }

    @Test
    fun `a stripe pattern alternates in x`() {
        val pattern = Stripes(WHITE, BLACK)
        assertThat(pattern.colourAt(Point(0, 0, 0))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(0.0, 0.0, 0.0))).isEqualTo(WHITE)
        assertThat(pattern.colourAt(Point(1, 0, 0))).isEqualTo(BLACK)
        assertThat(pattern.colourAt(Point(-0.1, 0.0, 0.0))).isEqualTo(BLACK)
        assertThat(pattern.colourAt(Point(-1, 0, 0))).isEqualTo(BLACK)
        assertThat(pattern.colourAt(Point(-1.1, 0.0, 0.0))).isEqualTo(WHITE)
    }

    @Test
    fun `lighting with pattern applied`() {
        val m = Material(pattern = Stripes(WHITE, BLACK), ambient = 1.0, diffuse = 0.0, specular = 0.0)
        val eyeV = Vector(0, 0, -1)
        val normal = Vector(0, 0,-1)
        val light = PointLight(Point(0, 0, -10), WHITE)
        val c1 = lighting(m , light, Point(0.9, 0.0, 0.0), eyeV, normal, dummyObj)
        val c2 = lighting(m , light, Point(1.1, 0.0, 0.0), eyeV, normal, dummyObj)
        assertThat(c1).isEqualTo(WHITE)
        assertThat(c2).isEqualTo(BLACK)
    }
}