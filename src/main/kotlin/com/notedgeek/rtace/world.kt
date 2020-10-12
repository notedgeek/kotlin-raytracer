package com.notedgeek.rtace

import com.notedgeek.rtace.obj.SceneObject

class World(val lights: List<PointLight>, objs: List<SceneObject>) {

    val objects = objs.toMutableList()

    constructor(light: PointLight, objs: List<SceneObject>) : this(listOf(light), objs)

    private var maxRecursiveDepth = 10

    fun intersections(ray: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        objects.forEach { result = intersectObject(it, ray, result) }
        return result
    }

    fun shadeHit(light: PointLight, comps: Comps, recursiveLevel: Int = 1): Colour {
        val shadowed = isShadowed(light, comps.overPoint)
        val surface = lighting(comps.obj.material, light, comps.point, comps.eyeV,
            comps.normal, comps.obj, shadowed)
        val reflected = reflectedColour(comps, recursiveLevel)
        return surface + reflected
    }

    fun colourAt(ray: Ray, recursiveLevel: Int = 1): Colour {
        if(recursiveLevel > maxRecursiveDepth) {
            return BLACK
        }
        val intersections = intersections(ray)
        val hit = hit(intersections)
        return if (hit == null) {
            BLACK
        } else {
            var lightCount = 0
            var result = Colour(0.0, 0.0, 0.0)
            lights.forEach {
                val comps = Comps(hit, ray)
                result += shadeHit(it, comps, recursiveLevel)
                lightCount++
            }
            result / lightCount.toDouble()
        }
    }

    fun isShadowed(light: PointLight, point: Point): Boolean {
        val v = light.position - point
        val distance = mag(v)
        val direction = normalise(v)
        val ray = Ray(point, direction)
        val hit = hit(intersections(ray))
        return hit != null && hit.t < distance
    }

    fun reflectedColour(comps: Comps, recursiveLevel: Int = 1): Colour {
        val reflective = comps.obj.material.reflective
        return if (closeTo(0.0, reflective)) {
            BLACK
        } else {
            val reflectRay = Ray(comps.overPoint, comps.reflectV)
            val color = colourAt(reflectRay, recursiveLevel + 1)
            color * reflective
        }
    }

    private fun intersectObject(obj: SceneObject, ray: Ray, currentIntersections: List<Intersection>): List<Intersection> {
        val list = obj.intersect(ray)
        val size = currentIntersections.size + list.size
        var currentListIndex = 0
        var listIndex = 0
        val result = ArrayList<Intersection>(size)
        for(i in 0 until size) {
            if (currentIntersections.isEmpty() || currentListIndex == currentIntersections.size) {
                result.add(list[listIndex])
                listIndex++
            } else if (list.isEmpty() || listIndex == list.size) {
                result.add(currentIntersections[currentListIndex])
                currentListIndex++
            } else if (currentIntersections[currentListIndex].t < list[listIndex].t) {
                result.add(currentIntersections[currentListIndex])
                currentListIndex++
            } else {
                result.add(list[listIndex])
                listIndex++
            }
        }
        return result
    }

    override fun toString(): String {
        return "World(lights=$lights, objects=$objects)"
    }

}
