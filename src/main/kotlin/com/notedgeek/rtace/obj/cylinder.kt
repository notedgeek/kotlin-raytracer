package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.*
import com.notedgeek.rtace.maths.closeTo
import kotlin.math.*

class Cylinder (
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null,
        private val min: Double = 0.0,
        private val max: Double = 1.0,
        private val cappedBottom: Boolean = false,
        private val cappedTop: Boolean = false

) : SceneObject(material, transform, parent,
    BoundingBox(-1.0, min, -1.0, 1.0, max, 1.0)) {

    override fun withTransform(transform: Matrix): Cylinder {
        return Cylinder(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun withMaterial(material: Material): Cylinder {
        return Cylinder(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun withParent(parent: SceneObject): Cylinder {
        return Cylinder(material, transform, parent, min, max, cappedBottom, cappedTop)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val xs = ArrayList<Intersection>()
        val a = localRay.direction.x.pow(2) + localRay.direction.z.pow(2)
        if(!closeTo(0.0, a)) {
            val b = 2 * localRay.origin.x * localRay.direction.x + 2 * localRay.origin.z * localRay.direction.z
            val c = localRay.origin.x.pow(2) + localRay.origin.z.pow(2) - 1
            val disc = b.pow(2) - 4 * a * c


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
            if(checkCap(ray, t)) {
                xs.add(Intersection(t, this))
            }
        }

        if(cappedTop) {
            val t = (max - ray.origin.y) / ray.direction.y
            if(checkCap(ray, t)) {
                xs.add(Intersection(t, this))
            }
        }
    }

    private fun checkCap(ray: Ray, t: Double): Boolean {
        val x = ray.origin.x + t * ray.direction.x
        val z = ray.origin.z + t * ray.direction.z
        return (x.pow(2) + z.pow(2)) <= 1
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {

        val dist = localPoint.x.pow(2) + localPoint.z.pow(2)

        if(dist < 1.0 && localPoint.y >= max - EPSILON) {
            return Vector(0, 1, 0)
        }

        if(dist < 1.0 && localPoint.y <= min + EPSILON) {
            return Vector(0, -1, 0)
        }

        return Vector(localPoint.x, 0.0, localPoint.z)
    }


    override fun equals(other: Any?) = other is Cylinder && material == other.material && transform == other.transform

}