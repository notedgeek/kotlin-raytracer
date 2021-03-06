@file:Suppress("EqualsOrHashCode","DuplicatedCode")

package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.Intersection
import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.maths.*
import com.notedgeek.rtrace.maths.closeTo
import com.notedgeek.rtrace.translation
import kotlin.math.*

class Cone (
        material: Material = Material(),
        transform: Matrix = translation(0.0, 1.0, 0.0),
        parent: SceneObject? = null,
        private val min: Double = -1.0,
        private val max: Double = 0.0,
        private val cappedBottom: Boolean = false,
        private val cappedTop: Boolean = false

) : SceneObject(material, transform, parent, BoundingBox(-1.0, min, -1.0, 1.0, max, 1.0)) {

    override fun withTransform(transform: Matrix): Cone {
        return Cone(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun withMaterial(material: Material): Cone {
        return Cone(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun withParent(parent: SceneObject): Cone {
        return Cone(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val origin = localRay.origin
        val direction = localRay.direction
        val a = direction.x.pow(2) - direction.y.pow(2) + direction.z.pow(2)
        val b = 2 * origin.x * direction.x - 2 * origin.y * direction.y + 2 * origin.z * direction.z
        val c = origin.x.pow(2) - origin.y.pow(2) + origin.z.pow(2)
        if(closeTo(0.0, a)) {
            return if(closeTo(0.0, b)) {
                emptyList()
            } else {
                val t = -c/(2 * b)
                val y0 = localRay.origin.y + t * localRay.direction.y
                if (min < y0 && y0 < max) {
                    listOf(Intersection(t, this))
                } else {
                    emptyList()
                }
            }
        }

        val disc = b.pow(2) - 4 * a * c

        val xs = ArrayList<Intersection>()

        if (disc >= 0.0) {
            var t0 = (-b - sqrt(disc)) / (2 * a)
            var t1 = (-b + sqrt(disc)) / (2 * a)

            if (t0 > t1) {
                t0 = t1.also { t1 = t0 }
            }


            val y0 = localRay.origin.y + t0 * localRay.direction.y
            if (min < y0 && y0 < max) {
                xs.add(Intersection(t0, this))
            }

            val y1 = localRay.origin.y + t1 * localRay.direction.y
            if (min < y1 && y1 < max) {
                xs.add(Intersection(t1, this))
            }
        }

        intersectCaps(localRay, xs)

        return xs.sortedBy { it.t }
    }

    private fun intersectCaps(ray: Ray, xs: MutableList<Intersection>) {
        if (closeTo(0.0, ray.direction.y)) {
            return
        }

        if(cappedBottom) {
            val t = (min - ray.origin.y) / ray.direction.y
            if(checkCap(ray, t, abs(min))) {
                xs.add(Intersection(t, this))
            }
        }

        if(cappedTop) {
            val t = (max - ray.origin.y) / ray.direction.y
            if(checkCap(ray, t, abs(max))) {
                xs.add(Intersection(t, this))
            }
        }
    }

    private fun checkCap(ray: Ray, t: Double, radius: Double): Boolean {
        val x = ray.origin.x + t * ray.direction.x
        val z = ray.origin.z + t * ray.direction.z
        return (x.pow(2) + z.pow(2)) <= radius
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        val dist = localPoint.x.pow(2) + localPoint.z.pow(2)

        if(dist < 1.0 && localPoint.y >= max - EPSILON) {
            return Vector(0, 1, 0)
        }

        if(dist < 1.0 && localPoint.y <= min + EPSILON) {
            return Vector(0, -1, 0)
        }

        val sign = if (localPoint.y <= 0) 1 else -1
        val y = sign * sqrt(localPoint.x.pow(2) + localPoint.z.pow(2)) + EPSILON / 2

        return Vector(localPoint.x, y, localPoint.z)
    }


    override fun equals(other: Any?) = other is Cone && material == other.material && transform == other.transform

}