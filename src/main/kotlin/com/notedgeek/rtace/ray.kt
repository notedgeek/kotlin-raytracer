package com.notedgeek.rtace

import com.notedgeek.rtace.obj.SceneObject

data class Intersection(val t: Double, val obj: SceneObject)

data class Ray(val origin: Point, val direction: Vector) {
    fun transform(t: Matrix) = Ray(t * origin, t * direction)
}

class Comps(intersection: Intersection, ray: Ray) {
    val t = intersection.t
    val obj = intersection.obj
    val point = position(ray, t)
    val eyeV = -ray.direction
    val normal: Vector
    val inside: Boolean
    val overPoint: Point
    val reflectV: Vector

    init {
        val normal = obj.normalAt(point)
        if (normal dot eyeV < 0.0) {
            inside = true
            this.normal = -normal
        } else {
            inside = false
            this.normal = normal
        }
        overPoint = point + normal * EPSILON
        reflectV = reflect(ray.direction, normal)
    }
}

fun position(ray: Ray, t: Double) = ray.origin + ray.direction * t

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