package com.notedgeek.rtace

import kotlin.math.sqrt

abstract class SceneObject<T>(val transform: Matrix = I) {

    val inverseTransform = -transform

    abstract fun intersects(r: Ray): List<Intersection>

    abstract fun withTransform(transform: Matrix): T

    fun transform(transform: Matrix): T {
        return withTransform(transform * this.transform)
    }
}


class Sphere(transformation: Matrix = I) : SceneObject<Sphere>(transformation) {

    override fun withTransform(transform: Matrix): Sphere {
        return Sphere(transform)
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
}

fun sphere() = Sphere()