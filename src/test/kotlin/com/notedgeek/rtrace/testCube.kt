package com.notedgeek.rtrace

import com.notedgeek.rtace.Intersection
import com.notedgeek.rtace.Point
import com.notedgeek.rtace.Ray
import com.notedgeek.rtace.Vector
import com.notedgeek.rtace.obj.Cube
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestCube {

    @Test
    fun `a ray intersects a cube`() {
       val c = Cube()
        class Example(val origin: Point, val direction: Vector, val t1: Double, val t2: Double)
        val examples = listOf(
            Example(Point(5.0, 0.5, 0.0), Vector(-1, 0, 0), 4.0, 6.0),
            Example(Point(-5.0, 0.5, 0.0), Vector(1, 0, 0), 4.0, 6.0),
            Example(Point(0.5, 5.0, 0.0), Vector(0, -1, 0), 4.0, 6.0),
            Example(Point(0.5, -5.0, 0.0), Vector(0, 1, 0), 4.0, 6.0),
            Example(Point(0.5, 0.0, 5.0), Vector(0, 0, -1), 4.0, 6.0),
            Example(Point(0.5, 0.0, -5.0), Vector(0, 0, 1), 4.0, 6.0),
            Example(Point(0.0, 0.5, 0.0), Vector(0, 0, 1), -1.0, 1.0)
        )
        for(example in examples) {
            val ray = Ray(example.origin, example.direction)
            val xs = c.localIntersect(ray)
            Assertions.assertThat(xs.size).isEqualTo(2)
            Assertions.assertThat(xs[0].t).isEqualTo(example.t1)
            Assertions.assertThat(xs[1].t).isEqualTo(example.t2)
        }
    }

    @Test
    fun `a ray misses a cube`() {
       val c = Cube()
        val examples = listOf(
            Point(-2, 0, 0) to Vector(0.2673, 0.5345, 0.8018),
            Point(0, -2, 0) to Vector(0.8018, 0.2673, 0.5345),
            Point(0, 0, -2) to Vector(0.5345, 0.8018, 0.2673),
            Point(2, 0, 2) to Vector(0, 0, -1),
            Point(0, 2, 2) to Vector(0, -1, 0),
            Point(2, 2, 0) to Vector(-1, 0, 0)
        )
        for(example in examples) {
            val ray = Ray(example.first, example.second)
            val xs = c.localIntersect(ray)
            Assertions.assertThat(xs.size).isEqualTo(0)
        }
    }

    @Test
    fun `normal on the surface of a cube`() {
       val c = Cube()
        class Example(val origin: Point, val direction: Vector, val t1: Double, val t2: Double)
        val examples = listOf(
            Point(1.0, 0.5, -0.8) to Vector(1, 0, 0),
            Point(-1.0, -0.2, 0.9) to Vector(-1, 0, 0),
            Point(-0.4, 1.0, -0.1) to Vector(0, 1, 0),
            Point(0.3, -1.0, -0.7) to Vector(0, -1, 0),
            Point(-0.6, 0.3, 1.0) to Vector(0, 0, 1),
            Point(0.4, 0.4, -1.0) to Vector(0, 0, -1),
            Point(1.0, 1.0, 1.0) to Vector(1, 0, 0),
            Point(-1.0, -1.0, -1.0) to Vector(-1, 0, 0)
        )
        for(example in examples) {
            val p = example.first
            Assertions.assertThat(c.localNormalAt(p, Intersection(0.0, c))).isEqualTo(example.second)
        }
    }
}