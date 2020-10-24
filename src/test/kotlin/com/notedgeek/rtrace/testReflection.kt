package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.Point
import com.notedgeek.rtace.maths.SQ2
import com.notedgeek.rtace.maths.Vector
import com.notedgeek.rtace.obj.Plane
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestReflection {

    @Test
    fun `reflectivity for default material`() {
        val m = Material()
        assertThat(m.reflective).isEqualTo(0.0)
    }

    @Test
    fun `precomputing the reflection vector`() {
        val shape = Plane()
        val ray = Ray(Point(0, 1, -1), Vector(0.0, -SQ2 / 2.0, SQ2 / 2.0))
        val i = Intersection(SQ2, shape)
        val comps = Comps(i, ray)
        assertThat(comps.reflectV).isEqualTo(Vector(0.0 , SQ2 / 2.0, SQ2 / 2.0))
    }

    @Test
    fun `reflective colour of a non reflective material`() {
        val w = defaultWorld()
        val ray = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val shape = w.objects[1].ambient(1.0)
        val i = Intersection(1.0, shape)
        val comps = Comps(i, ray)
        assertThat(w.reflectedColour(w.lights[0], comps)).isEqualTo(BLACK)
    }

    @Test
    fun `reflective colour of a reflective material`() {
        val w = defaultWorld()
        val shape = Plane().translate(0.0, -1.0, 0.0).reflective(0.5)
        val ray = Ray(Point(0, 0, -3), Vector(0.0, -SQ2 / 2, SQ2 / 2))
        w.objects.add(shape)
        val i = Intersection(SQ2, shape)
        val comps = Comps(i, ray)
        assertThat(w.reflectedColour(w.lights[0], comps)).isEqualTo(Colour(0.19033, 0.23792, 0.14275))
    }

    @Test
    fun `shade hit of a reflective material`() {
        val w = defaultWorld()
        val shape = Plane().translate(0.0, -1.0, 0.0).reflective(0.5)
        val ray = Ray(Point(0, 0, -3), Vector(0.0, -SQ2 / 2, SQ2 / 2))
        w.objects.add(shape)
        val i = Intersection(SQ2, shape)
        val comps = Comps(i, ray)
        assertThat(w.shadeHit(w.lights[0], comps)).isEqualTo(Colour(0.87676, 0.92434, 0.82917))
    }

}