package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import kotlin.math.sqrt

class Sphere(
    material: Material = Material(),
    transform: Matrix = I,
    parent: SceneObject? = null
) : SceneObject(material, transform, parent) {

    override fun withTransform(transform: Matrix): Sphere {
        return Sphere(material, transform, parent)
    }

    override fun withMaterial(material: Material): Sphere {
        return Sphere(material, transform, parent)
    }

    override fun withParent(parent: SceneObject): SceneObject {
        return Sphere(material, transform, parent)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val sphereToRay = localRay.origin - Point(0, 0, 0)
        val a = localRay.direction dot localRay.direction
        val b = 2 * (localRay.direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1
        val disc = b * b - 4 * a * c
        return if (disc < 0) {
            emptyList()
        } else {
            listOf(
                Intersection((-b - sqrt(disc)) / (2 * a), this),
                Intersection((-b + sqrt(disc)) / (2 * a), this)
            )
        }
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        return localPoint - Point(0, 0, 0)
    }

    override fun equals(other: Any?) = other is Sphere && material == other.material && transform == other.transform
}
