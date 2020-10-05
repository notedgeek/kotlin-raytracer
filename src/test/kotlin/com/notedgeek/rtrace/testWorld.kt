package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.Sphere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.junit.jupiter.api.Test

private val defaultWorld  = World(PointLight(Point(-10, 10, -10), WHITE), listOf(
    Sphere().withMaterial(Material(colour = Colour(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2)),
    Sphere().scale(0.5, 0.5, 0.5)
))


class TestWorld {

    @Test
    fun `default world`() {
        val light = PointLight(Point(-10, 10, -10), WHITE)
        val s1 = Sphere().withMaterial(Material(colour = Colour(0.8, 1.0, 0.6), diffuse = 0.7, specular = 0.2))
        val s2 = Sphere().scale(0.5, 0.5, 0.5)
        val w = defaultWorld
        assertThat(w.lights[0]).isEqualTo(light)
        assertThat(w.objects).contains(s1)
        assertThat(w.objects).contains(s2)
    }

    @Test
    fun `intersect a world with a ray`() {
        val w = defaultWorld
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
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
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = w.objects[0]
        val i = Intersection(4.0, s)
        val c = Comps(i, r)
        assertThat(w.shadeHit(w.lights[0], c)).isEqualTo(Colour(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `shading an intersection from the inside`() {
        val w = World(PointLight(Point(0.0, 0.25, 0.0), WHITE), defaultWorld.objects)
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val s = w.objects[1]
        val i = Intersection(0.5, s)
        val c = Comps(i, r)

        val amb = s.material.ambient
        // modified from 0.90498, 0.90498, 0.90498 - now that shading is considered
        assertThat(w.shadeHit(w.lights[0], c)).isEqualTo(Colour(amb, amb, amb))
    }

    @Test
    fun `the colour when a ray misses`() {
        val w = defaultWorld
        val r = Ray(Point(0, 0, -5), Vector(0, 1, 0))
        assertThat(w.colourAt(r)).isEqualTo(BLACK)
    }

    @Test
    fun `the colour when a ray hits`() {
        val w = defaultWorld
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        assertThat(w.colourAt(r)).isEqualTo(Colour(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `the Colour with an intersection behind the ray`() {
        val w = World(
            defaultWorld.lights[0],
            listOf(defaultWorld.objects[0].ambient(1.0), defaultWorld.objects[1].ambient(1.0)))
        val ray = Ray(Point(0.0, 0.0, 0.75), Vector(0, 0, -1))
        assertThat(w.colourAt(ray)).isEqualTo(defaultWorld.objects[1].material.colour)
    }

    @Test
    fun `there is no shadow when nothing is collinear with Point and light`() {
        val w = defaultWorld
        val p = Point(0, 10, 0)
        assertThat(w.isShadowed(w.lights[0], p)).isFalse
    }

    @Test
    fun `shadow when object is between point and light`() {
        val w = defaultWorld
        val p = Point(10, -10, 10)
        assertThat(w.isShadowed(w.lights[0], p)).isTrue
    }

    @Test
    fun `there is no shadow when object is behind light`() {
        val w = defaultWorld
        val p = Point(-20, -20, -20)
        assertThat(w.isShadowed(w.lights[0], p)).isFalse
    }

    @Test
    fun `there is no shadow when object is behind Point`() {
        val w = defaultWorld
        val p = Point(-2, 2, -2)
        assertThat(w.isShadowed(w.lights[0], p)).isFalse
    }

}