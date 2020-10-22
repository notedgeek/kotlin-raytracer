package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BoundingBox(
    val min: Point,
    val max: Point,
    val transform: Matrix = I
) {

    private val inverseTransform = -transform

    fun containsObject(obj: SceneObject): Boolean {
        val (pMin, pMax) = obj.bounds()
        return min.x <= pMin.x && max.x >= pMax.x &&
                min.y <= pMin.y && max.y >= pMax.y &&
                min.z <= pMin.z && max.z >= pMax.z

    }

    fun hitBy(ray: Ray): Boolean {
        val localRay = ray.transform(inverseTransform)
        val (xTMin, xTMax) = checkAxis(localRay.origin.x, localRay.direction.x, min.x, max.x)
        val (yTMin, yTMax) = checkAxis(localRay.origin.y, localRay.direction.y, min.y, max.y)
        val (zTMin, zTMax) = checkAxis(localRay.origin.z, localRay.direction.z, min.z, max.z)

        val tMin = max(xTMin, max(yTMin, zTMin))
        val tMax = min(xTMax, min(yTMax, zTMax))

        return tMin <= tMax
    }

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
        return BoundingBox(min, Point(midX, max.y, max.z)) to
                BoundingBox(Point(midX, min.y, min.z), max)
    }

    private fun splitY(): Pair<BoundingBox, BoundingBox> {
        val midY = min.y + (max.y - min.y) / 2
        return BoundingBox(min, Point(max.x, midY, max.z)) to
                BoundingBox(Point(min.x, midY, min.z), max)
    }

    private fun splitZ(): Pair<BoundingBox, BoundingBox> {
        val midZ = min.z + (max.z - min.z) / 2
        return BoundingBox(min, Point(max.x, max.y, midZ)) to
                BoundingBox(Point(min.x, min.y, midZ), max)
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