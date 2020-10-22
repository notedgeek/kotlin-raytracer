package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.Vector
import java.util.*

enum class CSGOperation {
    UNION, INTERSECT, DIFFERENCE
}

class CSG (
        pLeft: SceneObject,
        pRight: SceneObject,
        private val operation: CSGOperation,
        material: Material = Material(),
        parent: SceneObject? = null,
) : SceneObject(material, I, parent) {

    val left = pLeft.transform(transform)
    val right = pRight.transform(transform)

    override fun transform(transform: Matrix): CSG {
        return CSG(left.transform(transform), right.transform(transform),
                operation, material, parent)
    }

    override fun withTransform(transform: Matrix): Group {
        throw Exception("withTransform should not be called on CSGs")
    }

    override fun withMaterial(material: Material): CSG {
        return CSG(left.withMaterial(material), right.withMaterial(material), operation, material, parent)
    }

    override fun withParent(parent: SceneObject): CSG {
        return CSG(left, right, operation, material, parent)
    }

    override fun localIntersect(localRay: Ray) =
            filterIntersections(left.intersect(localRay).addIntersections(right.intersect(localRay)))

    override fun localNormalAt(localPoint: Point, intersection: Intersection) = Vector(0, 0, 0)

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