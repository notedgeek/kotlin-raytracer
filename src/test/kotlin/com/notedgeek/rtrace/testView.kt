package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import kotlin.math.PI
import com.notedgeek.rtace.EPSILON as EPSILON1

class TestView {

    @Test
    fun `transformation matrix for the default orientation`() {
        val from = Point(0, 0, 0)
        val to = Point(0, 0, -1)
        val up = Vector(0, 1, 0)
        assertThat(viewTransformation(from, to, up)).isEqualTo(I)
    }

    @Test
    fun `transformation matrix looking in the positive z direction`() {
        val from = Point(0, 0, 0)
        val to = Point(0, 0, 1)
        val up = Vector(0, 1, 0)
        assertThat(viewTransformation(from, to, up)).isEqualTo(scaling(-1.0, 1.0, -1.0))
    }

    @Test
    fun `view translation moves the world`() {
        val from = Point(0, 0, 8)
        val to = Point(0, 0, 0)
        val up = Vector(0, 1, 0)
        assertThat(viewTransformation(from, to, up)).isEqualTo(translation(0.0, 0.0, -8.0))
    }

    @Test
    fun `arbitrary view transformation`() {
        val from = Point(1, 3, 2)
        val to = Point(4, -2, 8)
        val up = Vector(1, 1, 0)
        assertThat(viewTransformation(from, to, up)).isEqualTo(Matrix(
           -0.50709,   0.50709,  0.67612,  -2.36643,
             0.76772,  0.60609,  0.12122,  -2.82843,
            -0.35857,  0.59761, -0.71714,   0.0,
             0.0,      0.0,      0.0,       1.0
        ))
    }

    @Test
    fun `constructing a camera`() {
        val width = 160
        val height = 120
        val fov = PI / 2.0
        val c = Camera(width, height, fov)
        assertThat(c.width).isEqualTo(160)
        assertThat(c.height).isEqualTo(120)
        assertThat(c.fov).isEqualTo(PI / 2.0)
        assertThat(c.transformation).isEqualTo(I)
    }

    @Test
    fun `pixel size for a horizontal canvas`() {
        assertThat(Camera(200, 125, PI / 2).pixelSize).isCloseTo(0.01, Offset.offset(EPSILON1))
    }

    @Test
    fun `pixel size for a vertical canvas`() {
        assertThat(Camera(125, 200, PI / 2).pixelSize).isCloseTo(0.01, Offset.offset(EPSILON1))
    }

    @Test
    fun `constructing a ray through the centre of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(100, 50)
        assertThat(r.origin).isEqualTo(Point(0, 0, 0))
        assertThat(r.direction).isEqualTo(Vector(0, 0, -1))
    }

    @Test
    fun `constructing a ray through the corner of the canvas`() {
        val c = Camera(201, 101, PI / 2)
        val r = c.rayForPixel(0, 0)
        assertThat(r.origin).isEqualTo(Point(0, 0, 0))
        assertThat(r.direction).isEqualTo(Vector(0.66519, 0.33259, -0.66851))
    }

    @Test
    fun `constructing a ray when the camera is transformed`() {
        val c = Camera(201, 101, PI / 2, rotationY(PI / 4) * translation(0.0, -2.0, 5.0))
        val r = c.rayForPixel(100, 50)
        assertThat(r.origin).isEqualTo(Point(0, 2, -5))
        assertThat(r.direction).isEqualTo(Vector(SQ2 / 2, 0.0, -SQ2 / 2))
    }
}