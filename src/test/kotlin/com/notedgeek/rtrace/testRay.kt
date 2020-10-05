package com.notedgeek.rtrace

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.Sphere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Test
import kotlin.math.PI

class TestRay {

    @Test
    fun `creating and querying a ray`() {
        val o = Point(1, 2, 3)
        val d = Vector(4, 5, 6)
        val r = Ray(o, d)
        assertThat(r.origin).isEqualTo(o)
        assertThat(r.direction).isEqualTo(d)
    }

    @Test
    fun `computing a point from a distance`() {
        val r = Ray(Point(2, 3, 4), Vector(1, 0, 0))
        assertThat(position(r, 0.0)).isEqualTo(Point(2, 3, 4))
        assertThat(position(r, 1.0)).isEqualTo(Point(3, 3, 4))
        assertThat(position(r, -1.0)).isEqualTo(Point(1, 3, 4))
        assertThat(position(r, 2.5)).isEqualTo(Point(4.5, 3.0, 4.0))
    }

    @Test
    fun `a ray intersects a sphere at two points`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(4.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(6.0, offset(EPSILON))
    }

    @Test
    fun `a ray intersects a sphere at a tangent`() {
        val r = Ray(Point(0, 1, -5), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(5.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(5.0, offset(EPSILON))
    }

    @Test
    fun `a ray misses a sphere`() {
        val r = Ray(Point(0, 2, -5), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(0)
    }

    @Test
    fun `a ray originates inside a sphere`() {
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(-1.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(1.0, offset(EPSILON))
    }

    @Test
    fun `a sphere is behind a ray`() {
        val r = Ray(Point(0, 0, 5), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(-6.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(-4.0, offset(EPSILON))
    }

    @Test
    fun `aggregating intersections`() {
        val s = Sphere()
        val i1 = Intersection(1.0, s)
        val i2 = Intersection(2.0, s)
        val xs = intersections(i1, i2)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].obj).isEqualTo(s)
        assertThat(xs[1].obj).isEqualTo(s)
    }

    @Test
    fun `intersect sets the object on the intersection`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere()
        val xs = s.localIntersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].obj).isEqualTo(s)
        assertThat(xs[1].obj).isEqualTo(s)
    }

    @Test
    fun `the hit when all intersections have positive t`() {
        val s = Sphere()
        val i1 = Intersection(1.0, s)
        val i2 = Intersection(2.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isEqualTo(i1)
    }

    @Test
    fun `the hit when some intersections have negative t`() {
        val s = Sphere()
        val i1 = Intersection(-1.0, s)
        val i2 = Intersection(1.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isEqualTo(i2)
    }

    @Test
    fun `the hit when all intersections have negative t`() {
        val s = Sphere()
        val i1 = Intersection(-2.0, s)
        val i2 = Intersection(-1.0, s)
        val xs = intersections(i1, i2)
        val i = hit(xs)
        assertThat(i).isNull()
    }

    @Test
    fun `the hit is the lowest non negative t`() {
        val s = Sphere()
        val i1 = Intersection(5.0, s)
        val i2 = Intersection(7.0, s)
        val i3 = Intersection(-3.0, s)
        val i4 = Intersection(2.0, s)
        val xs = intersections(i1, i2, i3, i4)
        val i = hit(xs)
        assertThat(i).isEqualTo(i4)
    }

    @Test
    fun `translating a ray`() {
        val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
        val t = translation(3.0, 4.0, 5.0)
        val r2 = r.transform(t)
        assertThat(r2).isEqualTo(Ray(Point(4, 6, 8), Vector(0, 1, 0)))
    }

    @Test
    fun `scaling a ray`() {
        val r = Ray(Point(1, 2, 3), Vector(0, 1, 0))
        val s = scaling(2.0, 3.0, 4.0)
        val r2 = r.transform(s)
        assertThat(r2).isEqualTo(Ray(Point(2, 6, 12), Vector(0, 3, 0)))
    }

    @Test
    fun `a spheres default transformation`() {
        val s = Sphere()
        assertThat(s.transform).isEqualTo(I)
    }

    @Test
    fun `transforming a sphere`() {
        val s = Sphere()
        val t = translation(2.0, 3.0, 4.0)
        val s1 = s.transform(t)
        assertThat(s1.transform).isEqualTo(t)
    }

    @Test
    fun `intersecting a scaled sphere with a ray`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere().transform(scaling(2.0, 2.0, 2.0))
        val xs = s.intersect(r)
        assertThat(xs.size).isEqualTo(2)
        assertThat(xs[0].t).isCloseTo(3.0, offset(EPSILON))
        assertThat(xs[1].t).isCloseTo(7.0, offset(EPSILON))
    }

    @Test
    fun `intersecting a translated sphere with a ray`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere().transform(translation(5.0, 0.0, 0.0))
        val xs = s.intersect(r)
        assertThat(xs.size).isEqualTo(0)
    }

    @Test
    fun `normal on a sphere at a point on the x axis` () {
        val s = Sphere()
        val n = s.normalAt(Point(1, 0, 0))
        assertThat(n).isEqualTo(Vector(1, 0, 0))
    }

    @Test
    fun `normal on a sphere at a point on the y axis` () {
        val s = Sphere()
        val n = s.normalAt(Point(0, 1, 0))
        assertThat(n).isEqualTo(Vector(0, 1, 0))
    }

    @Test
    fun `normal on a sphere at a point on the z axis` () {
        val s = Sphere()
        val n = s.normalAt(Point(0, 0, 1))
        assertThat(n).isEqualTo(Vector(0, 0, 1))
    }

    @Test
    fun `normal on a sphere at a non-axial point` () {
        val s = Sphere()
        val n = s.normalAt(Point(SQ3 / 3, SQ3 / 3, SQ3 / 3))
        assertThat(n).isEqualTo(Vector(SQ3 / 3, SQ3 / 3, SQ3 / 3))
    }

    @Test
    fun `normal is a normalized vector` () {
        val s = Sphere()
        val n = s.normalAt(Point(SQ3 / 3, SQ3 / 3, SQ3 / 3))
        assertThat(n).isEqualTo(normalise(n))
    }

    @Test
    fun `computing the normal on a translated sphere`() {
        val s = Sphere().translateY(1.0)
        val n = s.normalAt(Point(0.0, 1.70711, -0.70711))
        assertThat(n).isEqualTo(Vector(0.0, 0.70711, -0.70711))
    }

    @Test
    fun `computing the normal on a transformed sphere`() {
        val s = Sphere().rotateZ(PI / 5).scale(1.0, 0.5, 1.0)
        val n = s.normalAt(Point(0.0, SQ2 / 2, -SQ2 / 2))
        assertThat(n).isEqualTo(Vector(0.0, 0.97014, -0.24254))
    }

    @Test
    fun `reflecting a vector approaching at 45` () {
        val v = Vector(1, -1, 0)
        val n = Vector(0, 1, 0)
        val r = reflect(v, n)
        assertThat(r).isEqualTo(Vector(1, 1, 0))
    }

    @Test
    fun `reflecting a vector off a slanted surface` () {
        val v = Vector(0, -1, 0)
        val n = Vector(SQ2 / 2, SQ2 / 2, 0.0)
        val r = reflect(v, n)
        assertThat(r).isEqualTo(Vector(1, 0, 0))
    }

    @Test
    fun `pre-computing the state of an intersection`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere()
        val i = Intersection(4.0, s)
        val c = Comps(i, r)
        assertThat(c.t).isEqualTo(i.t)
        assertThat(c.obj).isEqualTo(s)
        assertThat(c.point).isEqualTo(Point(0, 0, -1))
        assertThat(c.eyeV).isEqualTo(Vector(0, 0, -1))
        assertThat(c.normal).isEqualTo(Vector(0, 0, -1))
    }

    @Test
    fun `the hit when an intersection occurs on the outside`() {
        val r = Ray(Point(0, 0, -5), Vector(0, 0, 1))
        val s = Sphere()
        val i = Intersection(4.0, s)
        val c = Comps(i, r)
        assertThat(c.inside).isFalse
    }

    @Test
    fun `the hit when an intersection occurs on the inside`() {
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val s = Sphere()
        val i = Intersection(1.0, s)
        val c = Comps(i, r)
        assertThat(c.point).isEqualTo(Point(0, 0, 1))
        assertThat(c.eyeV).isEqualTo(Vector(0, 0, -1))
        assertThat(c.normal).isEqualTo(Vector(0, 0, -1))
        assertThat(c.inside).isTrue
    }

}
