package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import kotlin.math.abs

class Plane(
    material: Material = Material(),
    transform: Matrix = I,
    parent: SceneObject? = null
) : SceneObject(material, transform, parent) {

    private val normal = Vector(0, 1, 0)

    override fun withTransform(transform: Matrix): Plane {
        return Plane(material, transform,  parent)
    }

    override fun withMaterial(material: Material): Plane {
        return Plane(material, transform, parent)
    }

    override fun withParent(parent: SceneObject): Plane {
        return Plane(material, transform, parent)
    }

    override fun localIntersect(localRay: Ray) =
        if (abs(localRay.direction.y) < EPSILON) {
            emptyList()
        } else {
            val t = -localRay.origin.y / localRay.direction.y
            listOf(Intersection(t, this))
        }


    override fun localNormalAt(localPoint: Point) = normal
}