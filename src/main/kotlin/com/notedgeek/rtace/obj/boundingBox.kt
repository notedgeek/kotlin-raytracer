package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BoundingBox(
    val min: Point,
    val max: Point,
    material: Material = Material(),
    transform: Matrix = I,
    parent: SceneObject? = null,
) : SceneObject(material, transform, parent) {

    fun containsObject(obj: SceneObject): Boolean {
        val (pMin, pMax) = obj.bounds()
        return min.x <= pMin.x && max.x >= pMax.x &&
                min.y <= pMin.y && max.y >= pMax.y &&
                min.z <= pMin.z && max.z >= pMax.z

    }

    override fun withTransform(transform: Matrix): BoundingBox {
        return BoundingBox(min, max, material, transform, parent)
    }

    override fun withMaterial(material: Material): BoundingBox {
        return BoundingBox(min, max, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): BoundingBox {
        return BoundingBox(min, max, material, transform, parent)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val (xTMin, xTMax) = checkAxis(localRay.origin.x, localRay.direction.x, min.x, max.x)
        val (yTMin, yTMax) = checkAxis(localRay.origin.y, localRay.direction.y, min.y, max.y)
        val (zTMin, zTMax) = checkAxis(localRay.origin.z, localRay.direction.z, min.z, max.z)

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

    override fun equals(other: Any?) = other is BoundingBox && material == other.material && transform == other.transform

    fun split(): Pair<BoundingBox, BoundingBox> {
        val xLen = max.x - min.x
        val yLen = max.y - min.y
        val zLen = max.z - min.z
        return when {
            xLen > yLen && xLen > zLen -> splitX()
            yLen > xLen && yLen > zLen -> splitY()
            zLen > xLen && zLen > yLen -> splitZ()
            else -> splitX()
        }
    }

    private fun splitX(): Pair<BoundingBox, BoundingBox> {
        val midX = min.x + (max.x - min.x) / 2
        return BoundingBox(min, Point(midX, max.y, max.z), material, transform, parent) to
                BoundingBox(Point(midX, min.y, min.z), max, material, transform, parent)
    }

    private fun splitY(): Pair<BoundingBox, BoundingBox> {
        val midY = min.y + (max.y - min.y) / 2
        return BoundingBox(min, Point(max.x, midY, max.z), material, transform, parent) to
                BoundingBox(Point(min.x, midY, min.z), max, material, transform, parent)
    }

    private fun splitZ(): Pair<BoundingBox, BoundingBox> {
        val midZ = min.z + (max.z - min.z) / 2
        return BoundingBox(min, Point(max.x, max.y, midZ), material, transform, parent) to
                BoundingBox(Point(min.x, min.y, midZ), max, material, transform, parent)
    }

    private fun checkAxis(origin: Double, direction: Double, min: Double, max: Double): Pair<Double, Double> {
        val tMinNumerator = (min - origin)
        val tMaxNumerator = (max - origin)

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