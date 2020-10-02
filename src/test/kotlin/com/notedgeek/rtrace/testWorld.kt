package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.objects.sphere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Test

private val defaultWorld  = World(pointLight(point(-10, 10, -10), WHITE), listOf(
    sphere().withMaterial(material(colour = colour(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2)),
    sphere().scale(0.5, 0.5, 0.5)
))


class TestWorld {

    @Test
    fun `default world`() {
        val light = pointLight(point(-10, 10, -10), WHITE)
        val s1 = sphere().withMaterial(material(colour = colour(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2))
        val s2 = sphere().scale(0.5, 0.5, 0.5)
        val w = defaultWorld
        assertThat(w.light).isEqualTo(light)
        assertThat(w.objects).contains(s1)
        assertThat(w.objects).contains(s2)
    }

    @Test
    fun `intersect a world with a ray`() {
        val w = defaultWorld
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val xs = w.intersections(r)
        assertThat(xs.size).isEqualTo(4)
        assertThat(xs[0].t).isCloseTo(4.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(4.5, offset(EPSILON))
        assertThat(xs[2].t).isCloseTo(5.5, offset(EPSILON))
        assertThat(xs[3].t).isCloseTo(6.0, offset(EPSILON))
    }
}