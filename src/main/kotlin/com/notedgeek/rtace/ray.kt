package com.notedgeek.rtace

import com.notedgeek.rtace.obj.SceneObject
import java.util.*
import kotlin.collections.ArrayList

class Intersection(val t: Double, val obj: SceneObject)

fun List<Intersection>.addIntersections(others: List<Intersection>): List<Intersection> {
    val size = this.size + others.size
    var currentListIndex = 0
    var listIndex = 0
    val result = ArrayList<Intersection>(size)
    for(i in 0 until size) {
        if (this.isEmpty() || currentListIndex == this.size) {
            result.add(others[listIndex])
            listIndex++
        } else if (others.isEmpty() || listIndex == others.size) {
            result.add(this[currentListIndex])
            currentListIndex++
        } else if (this[currentListIndex].t < others[listIndex].t) {
            result.add(this[currentListIndex])
            currentListIndex++
        } else {
            result.add(others[listIndex])
            listIndex++
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
        val normalAt = obj.normalAt(point)
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