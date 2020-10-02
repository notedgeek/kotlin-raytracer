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

    override fun intersects(r: Ray): List<Intersection> {
        val ray = r.transform(inverseTransform)
        val sphereToRay = ray.origin - point(0, 0, 0)
        val a = ray.direction dot ray.direction
        val b = 2 * (ray.direction dot sphereToRay)
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

    override fun normalAt(worldPoint: Point): Vector {
        val objectPoint = inverseTransform * worldPoint
        val objectNormal = objectPoint - point(0, 0, 0,)
        val worldNormal = transpose(inverseTransform) * objectNormal
        return normalise(worldNormal)
    }

    override fun equals(other: Any?) = other is Sphere && material == other.material && transform == other.transform
}

fun sphere() = Sphere()