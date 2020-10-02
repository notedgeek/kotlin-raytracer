package com.notedgeek.rtace.objects

import com.notedgeek.rtace.*
import kotlin.math.sqrt

class Sphere(
    material: Material = Material(),
    transformation: Matrix = I
) : SceneObject(
    material,
    transformation
) {

    override fun withTransform(transform: Matrix): Sphere {
        return Sphere(material, transform)
    }

    override fun material(material: Material): Sphere {
        return Sphere(material, transform)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val sphereToRay = localRay.origin - point(0, 0, 0)
        val a = localRay.direction dot localRay.direction
        val b = 2 * (localRay.direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1
        val disc = b * b - 4 * a * c
        return if (disc < 0) {
            emptyList()
        } else {
            listOf(
                intersection((-b - sqrt(disc)) / (2 * a), this),
                intersection((-b + sqrt(disc)) / (2 * a), this)
            )
        }
    }

    override fun localNormalAt(localPoint: Point): Vector {
        return localPoint - point(0, 0, 0)
    }

    override fun equals(other: Any?) = other is Sphere && material == other.material && transform == other.transform
}

fun sphere() = Sphere()