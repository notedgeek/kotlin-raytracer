package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.EPSILON
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.Vector
import com.notedgeek.rtrace.obj.Plane
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Test

class TestPlane {

    @Test
    fun `the normal of a plane is constant everywhere`() {
        val p = Plane()
        val dummyIntersection = Intersection(0.0, p)
        val n1 = p.localNormalAt(Point(0, 0, 0), dummyIntersection)
        val n2 = p.localNormalAt(Point(10, 0, -10), dummyIntersection)
        val n3 = p.localNormalAt(Point(-5, 0, 150), dummyIntersection)
        assertThat(n1).isEqualTo(Vector(0, 1, 0))
        assertThat(n2).isEqualTo(Vector(0, 1, 0))
        assertThat(n3).isEqualTo(Vector(0, 1, 0))
    }

    @Test
    fun `intersect with a ray parallel to the plane`() {
        val p = Plane()
        val r = Ray(Point(0, 10, 0), Vector(0, 0, 1))
        val xs = p.localIntersect(r)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `intersect with a coplanar ray`() {
        val p = Plane()
        val r = Ray(Point(0, 0, 0), Vector(0, 0, 1))
        val xs = p.localIntersect(r)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `a ray intersecting a plane from above`() {
        val p = Plane()
        val r = Ray(Point(0, 1, 0), Vector(0, -1, 0))
        val xs = p.localIntersect(r)
        assertThat(xs.size).isEqualTo(1)
        assertThat(xs[0].t).isCloseTo(1.0, offset(EPSILON))
        assertThat(xs[0].obj).isEqualTo(p)
    }

    @Test
    fun `a ray intersecting a plane from below`() {
        val p = Plane()
        val r = Ray(Point(0, -1, 0), Vector(0, 1, 0))
        val xs = p.localIntersect(r)
        assertThat(xs.size).isEqualTo(1)
        assertThat(xs[0].t).isCloseTo(1.0, offset(EPSILON))
        assertThat(xs[0].obj).isEqualTo(p)
    }
}