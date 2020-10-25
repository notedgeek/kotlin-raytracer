package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.Intersection
import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.addIntersections
import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.Vector
import java.util.*

enum class CSGOperation {
    UNION, INTERSECT, DIFFERENCE
}

class CSG (
        pLeft: SceneObject,
        pRight: SceneObject,
        private val operation: CSGOperation,
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null,
) : SceneObject(material, transform, parent) {

    val left = pLeft.withParent(this)
    val right = pRight.withParent(this)

    private val parentSpaceBounds: BoundingBox

    init {
        bounds = calculateBounds()
        parentSpaceBounds = bounds.transform(transform)
    }

    override fun parentSpaceBounds() = parentSpaceBounds

    private fun calculateBounds(): BoundingBox {
        var result = BoundingBox()
        result = result.addBoundingBox(left.parentSpaceBounds())
        result = result.addBoundingBox(right.parentSpaceBounds())
        return result
    }

    override fun withTransform(transform: Matrix): CSG {
        return CSG(left, right, operation, material, transform, parent)
    }

    override fun withMaterial(material: Material): CSG {
        return CSG(left.withMaterial(material), right.withMaterial(material), operation, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): CSG {
        return CSG(left, right, operation, material, transform, parent)
    }

    override fun localIntersect(localRay: Ray) = if (!bounds().hitBy(localRay)) {
        emptyList()
    } else {
        filterIntersections(left.intersect(localRay).addIntersections(right.intersect(localRay)))

    }

    override fun localNormalAt(localPoint: Point, hit: Intersection) = Vector(0, 0, 0)

    override fun includes(obj: SceneObject) = left.includes(obj) || right.includes(obj)

    private fun filterIntersections(xs: List<Intersection>): List<Intersection> {
        val result = LinkedList<Intersection>()

        var inL = false
        var inR = false

        for (intersection in xs) {
            val lHit = left.includes(intersection.obj)
            if (intersectionAllowed(lHit, inL, inR)) {
                result.add(intersection)
            }
            if (lHit) {
                inL = !inL
            } else {
                inR = !inR
            }
        }

        return result
    }

    private fun intersectionAllowed(lHit: Boolean, inL: Boolean, inR: Boolean) = when (operation) {
            CSGOperation.UNION -> (lHit && !inR) || (!lHit && !inL)
            CSGOperation.INTERSECT -> (lHit && inR) || (!lHit && inL)
            CSGOperation.DIFFERENCE -> (lHit && !inR) || (!lHit && inL)
        }

}