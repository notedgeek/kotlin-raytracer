package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.scaling
import com.notedgeek.rtace.translation
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TestRay {

    @Test
    fun `creating and querying a ray`() {
        val o = point(1, 2, 3)
        val d = vector(4, 5, 6)
        val r = ray(o, d)
        assertThat(r.origin).isEqualTo(o)
        assertThat(r.direction).isEqualTo(d)
    }

    @Test
    fun `computing a point from a distance`() {
        val r = ray(point(2, 3, 4), vector(1, 0, 0))
        assertThat(position(r, 0.0)).isEqualTo(point(2, 3, 4))
        assertThat(position(r, 1.0)).isEqualTo(point(3, 3, 4))
        assertThat(position(r, -1.0)).isEqualTo(point(1, 3, 4))
        assertThat(position(r, 2.5)).isEqualTo(point(4.5, 3.0, 4.0))
    }

    @Nested
    inner class Spheres {
        @Test
        fun `a ray intersects a sphere at two points`() {
            val r = ray(point(0, 0, -5), vector(0, 0, 1))
            val s = sphere()
            val xs = s.intersects(r)
            assertThat(xs.size).isEqualTo(2)
            assertThat(xs[0].t).isCloseTo(4.0, offset(EPSILON))
            assertThat(xs[1].t).isCloseTo(6.0, offset(EPSILON))
        }

        @Test
        fun `a ray intersects a sphere at a tangent`() {
            val r = ray(point(0, 1, -5), vector(0, 0, 1))
            val s = sphere()
            val xs = s.intersects(r)
            assertThat(xs.size).isEqualTo(2)
            assertThat(xs[0].t).isCloseTo(5.0, offset(EPSILON))
            assertThat(xs[1].t).isCloseTo(5.0, offset(EPSILON))
        }

        @Test
        fun `a ray misses a sphere`() {
            val r = ray(point(0, 2, -5), vector(0, 0, 1))
            val s = sphere()
            val xs = s.intersects(r)
            assertThat(xs.size).isEqualTo(0)
        }

        @Test
        fun `a ray originates inside a sphere`() {
            val r = ray(point(0, 0, 0), vector(0, 0, 1))
            val s = sphere()
            val xs = s.intersects(r)
            assertThat(xs.size).isEqualTo(2)
            assertThat(xs[0].t).isCloseTo(-1.0, offset(EPSILON))
            assertThat(xs[1].t).isCloseTo(1.0, offset(EPSILON))
        }

        @Test
        fun `a sphere is behind a ray`() {
            val r = ray(point(0, 0, 5), vector(0, 0, 1))
            val s = sphere()
            val xs = s.intersects(r)
            assertThat(xs.size).isEqualTo(2)
            assertThat(xs[0].t).isCloseTo(-6.0, offset(EPSILON))
            assertThat(xs[1].t).isCloseTo(-4.0, offset(EPSILON))
        }
    }

    @Test
    fun `aggregating intersections`() {
        val s = sphere()
        val i1 = intersection(1.0, s)
        val i2 = intersection(2.0, s)
        val xs = intersections(i1, i2)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].obj).isEqualTo(s)
        assertThat(xs[1].obj).isEqualTo(s)
    }

    @Test
    fun `intersect sets the object on the intersection`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()
        val xs = s.intersects(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].obj).isEqualTo(s)
        assertThat(xs[1].obj).isEqualTo(s)
    }

    @Test
    fun `the hit when all intersections have positive t`() {
        val s = sphere()
        val i1 = intersection(1.0, s)
        val i2 = intersection(2.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isEqualTo(i1)
    }

    @Test
    fun `the hit when some intersections have negative t`() {
        val s = sphere()
        val i1 = intersection(-1.0, s)
        val i2 = intersection(1.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isEqualTo(i2)
    }

    @Test
    fun `the hit when all intersections have negative t`() {
        val s = sphere()
        val i1 = intersection(-2.0, s)
        val i2 = intersection(-1.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isNull()
    }

    @Test
    fun `the hit is the lowest non negative t`() {
        val s = sphere()
        val i1 = intersection(5.0, s)
        val i2 = intersection(7.0, s)
        val i3 = intersection(-3.0, s)
        val i4 = intersection(2.0, s)
        val xs = intersections(i1, i2, i3, i4)
        val i = hit(xs)
        assertThat(i).isEqualTo(i4)
    }

    @Test
    fun `translating a ray`() {
        val r = ray(point(1, 2, 3), vector(0, 1, 0))
        val t = translation(3.0, 4.0, 5.0)
        val r2 = r.transform(t)
        assertThat(r2).isEqualTo(ray(point(4, 6, 8), vector(0, 1, 0)))
    }

    @Test
    fun `scaling a ray`() {
        val r = ray(point(1, 2, 3), vector(0, 1, 0))
        val s = scaling(2.0, 3.0, 4.0)
        val r2 = r.transform(s)
        assertThat(r2).isEqualTo(ray(point(2, 6, 12), vector(0, 3, 0)))
    }

    @Test
    fun `a spheres default transformation`() {
        val s = sphere()
        assertThat(s.transform).isEqualTo(I)
    }

    @Test
    fun `transforming a sphere`() {
        val s = sphere()
        val t = translation(2.0, 3.0, 4.0)
        val s1 = s.transform(t)
        assertThat(s1.transform).isEqualTo(t)
    }

    @Test
    fun `intersecting a scaled sphere with a ray`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere().transform(scaling(2.0, 2.0, 2.0))
        val xs = s.intersects(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(3.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(7.0, offset(EPSILON))
    }

    @Test
    fun `intersecting a translated sphere with a ray`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere().transform(translation(5.0, 0.0, 0.0))
        val xs = s.intersects(r)
        assertThat(xs.size).isEqualTo(0)
    }
}
