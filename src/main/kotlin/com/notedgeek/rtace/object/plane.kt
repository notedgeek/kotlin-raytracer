package com.notedgeek.rtace.`object`

import com.notedgeek.rtace.*
import kotlin.math.abs

class Plane(
    material: Material = Material(),
    transformation: Matrix = I
) : SceneObject(material, transformation) {

    private val normal = Vector(0, 1, 0)

    override fun withTransform(transform: Matrix): SceneObject {
        return Plane(material, transform)
    }

    override fun withMaterial(material: Material): SceneObject {
        return Plane(material, transform)
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