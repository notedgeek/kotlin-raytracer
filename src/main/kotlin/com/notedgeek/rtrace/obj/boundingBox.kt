@file:Suppress("DuplicatedCode")

package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.maths.EPSILON
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class BoundingBox(
        private val min: Point = Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
        private val max: Point = Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
) {

    constructor(xMin: Double, yMin: Double, zMin: Double, xMax: Double, yMax: Double, zMax: Double) : this(
        Point(xMin, yMin, zMin), Point(xMax, yMax, zMax)
    )

    private fun addPoint(point: Point): BoundingBox = BoundingBox(
            Point(min(min.x, point.x), min(min.y, point.y), min(min.z, point.z)),
            Point(max(max.x, point.x), max(max.y, point.y), max(max.z, point.z))
    )

    fun addBoundingBox(boundingBox: BoundingBox) = addPoint(boundingBox.min).addPoint(boundingBox.max)

    fun transform(transform: Matrix): BoundingBox {
        var boundingBox = BoundingBox()
        for(point in arrayOf(
                min,
                Point(min.x, min.y, max.z),
                Point(min.x, max.y, min.z),
                Point(min.x, max.y, max.z),
                Point(max.x, min.y, min.z),
                Point(max.x, min.y, max.z),
                Point(max.x, max.y, min.z),
                max
        )) {
            boundingBox = boundingBox.addPoint(transform * point)
        }
        return boundingBox
    }

    fun containsObject(obj: SceneObject): Boolean {
        val bb = obj.parentSpaceBounds()
        val pMin = bb.min
        val pMax = bb.max
        return min.x <= pMin.x && max.x >= pMax.x &&
                min.y <= pMin.y && max.y >= pMax.y &&
                min.z <= pMin.z && max.z >= pMax.z

    }

    fun hitBy(localRay: Ray): Boolean {
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
            tMin = if(tMinNumerator == 0.0) 0.0 else tMinNumerator * Double.POSITIVE_INFINITY
            tMax = if(tMaxNumerator == 0.0) 0.0 else tMaxNumerator * Double.POSITIVE_INFINITY
        }
        return if (tMin <= tMax) tMin to tMax else tMax to tMin
    }
}