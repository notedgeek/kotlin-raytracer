package com.notedgeek.rtace

import com.notedgeek.rtace.obj.SceneObject
import java.util.*
import kotlin.collections.ArrayList

class Intersection(val t: Double, val obj: SceneObject, val u: Double = 0.0, val v: Double = 0.0)

fun List<Intersection>.addIntersections(others: List<Intersection>): List<Intersection> {
    val size = this.size + others.size
    var thisIndex = 0
    var otherIndex = 0
    val result = ArrayList<Intersection>(size)
    for(i in 0 until size) {
        if (this.isEmpty() || thisIndex == this.size) {
            result.add(others[otherIndex])
            otherIndex++
        } else if (others.isEmpty() || otherIndex == others.size) {
            result.add(this[thisIndex])
            thisIndex++
        } else if (this[thisIndex].t < others[otherIndex].t) {
            result.add(this[thisIndex])
            thisIndex++
        } else {
            result.add(others[otherIndex])
            otherIndex++
        }
    }
    return result
}

data class Ray(val origin: Point, val direction: Vector) {
    fun transform(t: Matrix) = Ray(t * origin, t * direction)
}

class Comps(hit: Intersection, ray: Ray, intersections: List<Intersection> = listOf(hit)) {
    val t = hit.t
    val obj = hit.obj
    val point = position(ray, t)
    val eyeV = -ray.direction
    val normal: Vector
    val inside: Boolean
    val overPoint: Point
    val underPoint: Point
    val reflectV: Vector
    var n1: Double = 1.0
    var n2: Double = 1.0

    init {
        val normalAt = obj.normalAt(point, hit)
        if (normalAt dot eyeV < 0.0) {
            inside = true
            normal = -normalAt
        } else {
            inside = false
            normal = normalAt
        }
        overPoint = point + normal * EPSILON
        underPoint = point - normal * EPSILON
        reflectV = reflect(ray.direction, normal)

        val containers = LinkedList<SceneObject>()

        for (intersection in intersections) {
            if(intersection == hit) {
                n1 = if(containers.isEmpty()) {
                    1.0
                } else {
                    containers.last.material.refractiveIndex
                }
            }

            if(containers.contains(intersection.obj)) {
                containers.remove(intersection.obj)
            } else {
                containers.addLast(intersection.obj)
            }

            if(intersection == hit) {
                n2 = if(containers.isEmpty()) {
                    1.0
                } else {
                    containers.last.material.refractiveIndex
                }
                break
            }
        }
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