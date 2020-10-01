package com.notedgeek.rtace

import com.notedgeek.rtace.`object`.SceneObject

data class Intersection(val t: Double, val obj: SceneObject<*>)

data class Ray(val origin: Point, val direction: Vector) {
    fun transform(t: Matrix) = Ray(t * origin, t * direction)
}

fun ray(origin: Point, direction: Vector) = Ray(origin, direction)

fun position(ray: Ray, t: Double) = ray.origin + ray.direction * t

fun intersection(t: Double, obj: SceneObject<*>) = Intersection(t, obj)

fun intersections(vararg elems: Intersection): List<Intersection> {
    val result = ArrayList<Intersection>(elems.size)
    result.addAll(elems)
    result.sortBy { it.t }
    return result
}

fun hit(intersections: Iterable<Intersection>): Intersection? {
    intersections.forEach{if (it.t >= 0.0) {
        return it
    } }
    return null
}

fun reflect(incoming: Vector, normal: Vector) = incoming - normal * 2 * (incoming dot normal)