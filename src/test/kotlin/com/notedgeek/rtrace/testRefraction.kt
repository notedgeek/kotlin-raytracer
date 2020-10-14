package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.Plane
import com.notedgeek.rtace.obj.Sphere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.*
import org.junit.jupiter.api.Test

class TestRefraction {

    private val glassSphere = Sphere().withMaterial(Material()
        .withTransparency(1.0)
        .withRefractiveIndex(1.5))

    @Test
    fun `transparency and refractive index of default material`() {
        val m = Material()
        assertThat(m.transparency).isEqualTo(0.0)
        assertThat(m.refractiveIndex).isEqualTo(1.0)
    }

    @Test
    fun `finding n1 and n1 at various intersections`() {
        val a = glassSphere.scale(2.0, 2.0, 2.0)
            .withMaterial(glassSphere.material.withRefractiveIndex(1.5))
        val b = glassSphere.translateZ(-0.25)
            .withMaterial(glassSphere.material.withRefractiveIndex(2.0))
        val c = glassSphere.translateZ(0.25)
            .withMaterial(glassSphere.material.withRefractiveIndex(2.5))
        val ray = Ray(Point(0, 0, -4), Vector(0, 0, 1))
        val intersections = listOf(Intersection(2.0, a), Intersection(2.75, b), Intersection(3.25, c),
            Intersection(4.75, b), Intersection(5.25, c), Intersection(6.0, a))

        val expectedValues = listOf(
            1.0 to 1.5,
            1.5 to 2.0,
            2.0 to 2.5,
            2.5 to 2.5,
            2.5 to 1.5,
            1.5 to 1.0
        )

        for((index, intersection) in intersections.withIndex()) {
            val comps = Comps(intersection, ray, intersections)
            assertThat(comps.n1).isCloseTo(expectedValues[index].first, offset(EPSILON))
            assertThat(comps.n2).isCloseTo(expectedValues[index].second, offset(EPSILON))
        }
    }

    @Test
    fun `under point is offset below the surface`() {
        val ray = Ray(Point(0, 0, -5), Vector(0, 0,1))
        val shape = glassSphere.translateZ(1.0)
        val i = Intersection(5.0, shape)
        val xs = listOf(i)
        val comps = Comps(i, ray, xs)
        assertThat(comps.underPoint.z).isGreaterThan(EPSILON / 2)
        assertThat(comps.point.z).isLessThan(comps.underPoint.z)
    }

    @Test
    fun `refracted colour with opaque surface`() {
        val w = defaultWorld()
        val shape = w.objects[0]
        val ray = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val xs = listOf(Intersection(4.0, shape), Intersection(6.0, shape))
        val comps = Comps(xs[0], ray, xs)
        assertThat(w.refractedColour(w.lights[0], comps)).isEqualTo(BLACK)
    }

    @Test
    fun `refracted colour at maximum recursive depth`() {
        val w = defaultWorld()
        var shape = w.objects[0]
        shape = shape.withMaterial(shape.material.withTransparency(1.0).withRefractiveIndex(1.5))
        w.objects[0] = shape
        val ray = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val xs = listOf(Intersection(4.0, shape), Intersection(6.0, shape))
        val comps = Comps(xs[0], ray, xs)
        assertThat(w.refractedColour(w.lights[0], comps, 0)).isEqualTo(BLACK)
    }

    @Test
    fun `refracted colour under total internal reflection`() {
        val w = defaultWorld()
        var shape = w.objects[0]
        shape = shape.withMaterial(shape.material.withTransparency(1.0).withRefractiveIndex(1.5))
        w.objects[0] = shape
        val ray = Ray(Point(0.0, 0.0, SQ2 / 2), Vector(0, 1, 0))
        val xs = listOf(Intersection(-SQ2 / 2, shape), Intersection(SQ2 / 2, shape))
        val comps = Comps(xs[1], ray, xs)
        assertThat(w.refractedColour(w.lights[0], comps)).isEqualTo(BLACK)
    }

    @Test
    fun `shadeHit with a transparent material`() {
        val w = defaultWorld()
        val floor = Plane().translateY(-1.0)
            .withMaterial(Material().withTransparency(0.5).withRefractiveIndex(1.5))
        w.objects.add(floor)
        val sphere = Sphere().translate(0.0, -3.5, -0.5)
            .withMaterial(Material().withColour(Colour(1.0, 0.0, 0.0)).withAmbient(0.5))
        w.objects.add(sphere)
        val ray = Ray(Point(0, 0, -3), Vector(0.0, -SQ2 / 2, SQ2 / 2))
        val xs = listOf(Intersection(SQ2, floor))
        val comps = Comps(xs[0], ray, xs)
        val c = w.shadeHit(w.lights[0], comps, 5)
        assertThat(c).isEqualTo(Colour(0.93642, 0.68642, 0.68642))
    }
}