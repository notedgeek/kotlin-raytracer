@file:Suppress("EqualsOrHashCode", "DuplicatedCode")

package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.Intersection
import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.maths.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Cube(
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null,
) : SceneObject(material, transform, parent,
    BoundingBox(-1.0, -1.0, -1.0, 1.0, 1.0, 1.0)) {

    override fun withTransform(transform: Matrix): Cube {
        return Cube(material, transform, parent)
    }

    override fun withMaterial(material: Material): Cube {
        return Cube(material, transform, parent)
    }

    override fun withParent(parent: SceneObject): Cube {
        return Cube(material, transform, parent)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val (xTMin, xTMax) = checkAxis(localRay.origin.x, localRay.direction.x)
        val (yTMin, yTMax) = checkAxis(localRay.origin.y, localRay.direction.y)
        val (zTMin, zTMax) = checkAxis(localRay.origin.z, localRay.direction.z)

        val tMin = max(xTMin, max(yTMin, zTMin))
        val tMax = min(xTMax, min(yTMax, zTMax))

        return if (tMin <= tMax)
            listOf(Intersection(tMin, this), Intersection(tMax, this)) else emptyList()
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection) =
        when (max(abs(localPoint.x), max(abs(localPoint.y), abs(localPoint.z)))) {
            abs(localPoint.x) -> Vector(localPoint.x, 0.0, 0.0)
            abs(localPoint.y) -> Vector(0.0, localPoint.y, 0.0)
            else -> Vector(0.0, 0.0, localPoint.z)
        }

    override fun equals(other: Any?) = other is Cube && material == other.material && transform == other.transform

    private fun checkAxis(origin: Double, direction: Double): Pair<Double, Double> {
        val tMinNumerator = (-1.0 - origin)
        val tMaxNumerator = (1.0 - origin)

        val tMin: Double
        val tMax: Double
        if (abs(direction) >= EPSILON) {
            tMin = tMinNumerator / direction
            tMax = tMaxNumerator / direction
        } else {
            tMin = tMinNumerator * Double.POSITIVE_INFINITY
            tMax = tMaxNumerator * Double.POSITIVE_INFINITY
        }
        return if (tMin <= tMax) tMin to tMax else tMax to tMin
    }
}