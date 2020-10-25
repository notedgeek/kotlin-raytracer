package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.closeTo
import com.notedgeek.rtrace.maths.mag
import com.notedgeek.rtrace.maths.normalise
import com.notedgeek.rtrace.obj.SceneObject
import kotlin.math.sqrt

private var MAX_RECURSIVE_DEPTH = 7

class World(val lights: List<PointLight>, objs: List<SceneObject>) {

    val objects = objs.toMutableList()

    constructor(light: PointLight, objs: List<SceneObject>) : this(listOf(light), objs)


    fun intersections(ray: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        objects.forEach { result = result.addIntersections(it.intersect(ray)) }
        return result
    }

    fun shadeHit(light: PointLight, comps: Comps, remaining: Int = MAX_RECURSIVE_DEPTH): Colour {
        val shadowed = isShadowed(light, comps.overPoint)
        val surface = lighting(comps.obj.material, light, comps.point, comps.eyeV,
            comps.normal, comps.obj, shadowed)
        val reflected = reflectedColour(light, comps, remaining)
        val refracted = refractedColour(light, comps, remaining)
        return surface + reflected + refracted
    }

    fun colourAt(ray: Ray): Colour {
        var lightCount = 0
        var result = Colour(0.0, 0.0, 0.0)
        val intersections = intersections(ray)
        val hit = hit(intersections)
        lights.forEach {
            result += colourAtForPointLight(it, ray, MAX_RECURSIVE_DEPTH, intersections, hit)
            lightCount++
        }
        return result / lightCount.toDouble()
    }

    fun colourAtForPointLight(light: PointLight, ray: Ray, remaining: Int = MAX_RECURSIVE_DEPTH,
                              pIntersections: List<Intersection>? = null, pHit: Intersection? = null): Colour {
        if(remaining == 0) {
            return BLACK
        }
        val intersections: List<Intersection>
        val hit: Intersection?
        if(pIntersections == null) {
            intersections = intersections(ray)
            hit = hit(intersections)
        } else {
            intersections = pIntersections
            hit = pHit
        }

        return if (hit == null) {
            BLACK
        } else {
            val comps = Comps(hit, ray, intersections)
            return shadeHit(light, comps, remaining)
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

    fun reflectedColour(light: PointLight, comps: Comps, remaining: Int = MAX_RECURSIVE_DEPTH): Colour {
        val reflective = comps.obj.material.reflective
        return if (closeTo(0.0, reflective)) {
            BLACK
        } else {
            val reflectRay = Ray(comps.overPoint, comps.reflectV)
            return colourAtForPointLight(light, reflectRay, remaining - 1) * reflective
        }
    }

    fun refractedColour(light: PointLight, comps: Comps, remaining: Int = MAX_RECURSIVE_DEPTH): Colour {
        val transparency = comps.obj.material.transparency
        if (remaining == 0 || closeTo(transparency, 0.0)) {
            return BLACK
        }

        val ratio = comps.n1 / comps.n2
        val cosI = comps.eyeV dot comps.normal
        val sin2T = ratio * ratio * (1 - cosI * cosI)

        if(sin2T > 1.0) {
            return BLACK
        }

        val cosT = sqrt(1.0 - sin2T)
        val direction = comps.normal * (ratio * cosI - cosT) - comps.eyeV * ratio
        val refractRay = Ray(comps.underPoint, direction)

        return colourAtForPointLight(light, refractRay, remaining - 1) * transparency
    }

    override fun toString(): String {
        return "World(lights=$lights, objects=$objects)"
    }

}
