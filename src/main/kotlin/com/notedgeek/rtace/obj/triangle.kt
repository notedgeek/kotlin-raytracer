package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class Triangle(
        val p1: Point,
        val p2: Point,
        val p3: Point,
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null,
) : SceneObject(material, transform, parent) {

    val minX = min((p1.x), min(p2.x, p3.x))
    val maxX = max((p1.x), max(p2.x, p3.x))
    val minY = min((p1.y), min(p2.y, p3.y))
    val maxY = max((p1.y), max(p2.y, p3.y))
    val minZ = min((p1.z), min(p2.z, p3.z))
    val maxZ = max((p1.z), max(p2.z, p3.z))

    private val e1 = p2 - p1
    private val e2 = p3 - p1

    val normal = normalise(e2 cross e1)

    override fun bounds(): Pair<Point, Point> {
        return Pair(Point(minX, minY, minZ), Point(maxX, maxY, maxZ))
    }

    override fun withTransform(transform: Matrix): Triangle {
        return Triangle(p1, p2, p3, material, transform, parent)
    }

    override fun withMaterial(material: Material): Triangle {
        return Triangle(p1, p2, p3, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): Triangle {
        return Triangle(p1, p2, p3, material, transform, parent)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val dirCrossE2 = localRay.direction cross e2
        val det = e1 dot dirCrossE2
        if (abs(det) < EPSILON) {
            return emptyList()
        }
        val f = 1.0 / det
        val p1ToOrigin = localRay.origin - p1
        val u = f * (p1ToOrigin dot dirCrossE2)
        if(u < 0.0 || u > 1.0) {
            return emptyList()
        }
        val originCrossE1 = p1ToOrigin cross e1
        val v = f * (localRay.direction dot originCrossE1)
        if(v < 0.0 || (u + v) > 1) {
            return emptyList()
        }
        val t = f * (e2 dot originCrossE1)
        return listOf(Intersection(t, this, u, v))
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection) = normal

    override fun equals(other: Any?) = other is Triangle && p1 == p1 && p2 == p2 && p3 == p3 &&
            material == other.material && transform == other.transform

}