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

    @Test
    fun `shading an intersection`() {
        val w = defaultWorld
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = w.objects[0]
        val i = intersection(4.0, s)
        val c = comps(i, r)
        assertThat(w.shadeHit(c)).isEqualTo(colour(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `shading an intersection from the inside`() {
        val w = world(pointLight(point(0.0, 0.25, 0.0), WHITE), defaultWorld.objects)
        val r = ray(point(0, 0, 0), vector(0, 0, 1))
        val s = w.objects[1]
        val i = intersection(0.5, s)
        val c = comps(i, r)
        assertThat(w.shadeHit(c)).isEqualTo(colour(0.90498, 0.90498, 0.90498))
    }

    @Test
    fun `the colour when a ray misses`() {
        val w = defaultWorld
        val r = ray(point(0, 0, -5), vector(0, 1, 0))
        assertThat(w.colourAt(r)).isEqualTo(BLACK)
    }

    @Test
    fun `the colour when a ray hits`() {
        val w = defaultWorld
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        assertThat(w.colourAt(r)).isEqualTo(colour(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `the colour with an intersection behind the ray`() {
        val w = world(
            defaultWorld.light,
            listOf(defaultWorld.objects[0].withAmbient(1.0), defaultWorld.objects[1].withAmbient(1.0)))
        val ray = ray(point(0.0, 0.0, 0.75), vector(0, 0, -1))
        assertThat(w.colourAt(ray)).isEqualTo(defaultWorld.objects[1].material.colour)
    }
}