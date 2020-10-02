package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.objects.sphere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Test
import kotlin.math.PI

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

    @Test
    fun `a ray intersects a sphere at two points`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(4.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(6.0, offset(EPSILON))
    }

    @Test
    fun `a ray intersects a sphere at a tangent`() {
        val r = ray(point(0, 1, -5), vector(0, 0, 1))
        val s = sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(5.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(5.0, offset(EPSILON))
    }

    @Test
    fun `a ray misses a sphere`() {
        val r = ray(point(0, 2, -5), vector(0, 0, 1))
        val s = sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(0)
    }

    @Test
    fun `a ray originates inside a sphere`() {
        val r = ray(point(0, 0, 0), vector(0, 0, 1))
        val s = sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(-1.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(1.0, offset(EPSILON))
    }

    @Test
    fun `a sphere is behind a ray`() {
        val r = ray(point(0, 0, 5), vector(0, 0, 1))
        val s = sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(-6.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(-4.0, offset(EPSILON))
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
        val xs = s.localIntersect(r)
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
        val xs = s.intersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(3.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(7.0, offset(EPSILON))
    }

    @Test
    fun `intersecting a translated sphere with a ray`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere().transform(translation(5.0, 0.0, 0.0))
        val xs = s.intersect(r)
        assertThat(xs.size).isEqualTo(0)
    }

    @Test
    fun `normal on a sphere at a point on the x axis` () {
        val s = sphere()
        val n = s.normalAt(point(1, 0, 0))
        assertThat(n).isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `normal on a sphere at a point on the y axis` () {
        val s = sphere()
        val n = s.normalAt(point(0, 1, 0))
        assertThat(n).isEqualTo(vector(0, 1, 0))
    }

    @Test
    fun `normal on a sphere at a point on the z axis` () {
        val s = sphere()
        val n = s.normalAt(point(0, 0, 1))
        assertThat(n).isEqualTo(vector(0, 0, 1))
    }

    @Test
    fun `normal on a sphere at a non-axial point` () {
        val s = sphere()
        val n = s.normalAt(point(SQ3 / 3, SQ3 / 3, SQ3 / 3))
        assertThat(n).isEqualTo(vector(SQ3 / 3, SQ3 / 3, SQ3 / 3))
    }

    @Test
    fun `normal is a normalized vector` () {
        val s = sphere()
        val n = s.normalAt(point(SQ3 / 3, SQ3 / 3, SQ3 / 3))
        assertThat(n).isEqualTo(normalise(n))
    }

    @Test
    fun `computing the normal on a translated sphere`() {
        val s = sphere().translateY(1.0)
        val n = s.normalAt(point(0.0, 1.70711, -0.70711))
        assertThat(n).isEqualTo(vector(0.0, 0.70711, -0.70711))
    }

    @Test
    fun `computing the normal on a transformed sphere`() {
        val s = sphere().rotateZ(PI / 5).scale(1.0, 0.5, 1.0)
        val n = s.normalAt(point(0.0, SQ2 / 2, -SQ2 / 2))
        assertThat(n).isEqualTo(vector(0.0, 0.97014, -0.24254))
    }

    @Test
    fun `reflecting a vector approaching at 45` () {
        val v = vector(1, -1, 0)
        val n = vector(0, 1, 0)
        val r = reflect(v, n)
        assertThat(r).isEqualTo(vector(1, 1, 0))
    }

    @Test
    fun `reflecting a vector off a slanted surface` () {
        val v = vector(0, -1, 0)
        val n = vector(SQ2 / 2, SQ2 / 2, 0.0)
        val r = reflect(v, n)
        assertThat(r).isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `pre-computing the state of an intersection`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()
        val i = intersection(4.0, s)
        val c = comps(i, r)
        assertThat(c.t).isEqualTo(i.t)
        assertThat(c.obj).isEqualTo(s)
        assertThat(c.point).isEqualTo(point(0, 0, -1))
        assertThat(c.eyeV).isEqualTo(vector(0, 0, -1))
        assertThat(c.normal).isEqualTo(vector(0, 0, -1))
    }

    @Test
    fun `the hit when an intersection occurs on the outside`() {
        val r = ray(point(0, 0, -5), vector(0, 0, 1))
        val s = sphere()
        val i = intersection(4.0, s)
        val c = comps(i, r)
        assertThat(c.inside).isFalse
    }

    @Test
    fun `the hit when an intersection occurs on the inside`() {
        val r = ray(point(0, 0, 0), vector(0, 0, 1))
        val s = sphere()
        val i = intersection(1.0, s)
        val c = comps(i, r)
        assertThat(c.point).isEqualTo(point(0, 0, 1))
        assertThat(c.eyeV).isEqualTo(vector(0, 0, -1))
        assertThat(c.normal).isEqualTo(vector(0, 0, -1))
        assertThat(c.inside).isTrue

    }

}
