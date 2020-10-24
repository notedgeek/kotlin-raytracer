package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.*
import com.notedgeek.rtace.maths.Vector
import com.notedgeek.rtace.maths.closeTo
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class Torus(
        val rMaj: Double = 1.0,
        var rMin: Double = 1 / 3.0,
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null
) : SceneObject(material, transform, parent,
        BoundingBox(-(rMaj + rMin), -rMin, -(rMaj + rMin), rMaj + rMin, rMin, rMaj + rMin)) {

    override fun withTransform(transform: Matrix): Torus {
        return Torus(rMaj, rMin, material, transform, parent)
    }

    override fun withMaterial(material: Material): Torus {
        return Torus(rMaj, rMin, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): SceneObject {
        return Torus(rMaj, rMin, material, transform, parent)
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {

        val ox = localRay.origin.x
        val oy = localRay.origin.y
        val oz = localRay.origin.z
        val dx = localRay.direction.x
        val dy = localRay.direction.y
        val dz = localRay.direction.z

        val sumDSqd = dx * dx + dy * dy + dz * dz
        val sumOD = ox * dx + oy * dy + oz * dz
        val oMM = ox * ox + oy * oy + oz * oz - rMaj * rMaj - rMin * rMin

        val coeffs = doubleArrayOf(
                oMM * oMM - 4 * rMaj * rMaj * (rMin * rMin - oy * oy),
                4 * oMM * sumOD + 8 * rMaj * rMaj * oy * dy,
                2 * sumDSqd * oMM + 4 * (sumOD * sumOD + rMaj * rMaj * dy * dy),
                4 * sumDSqd * sumOD,
                sumDSqd * sumDSqd
        )

        val result = LinkedList<Intersection>()
        val solutions = solve4(coeffs)
        for(solution in solutions) {
            result.add(Intersection(solution, this))
        }
        result.sortBy { it.t }
        return result
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        val paramSquared = rMaj * rMaj + rMin * rMin
        val x = localPoint.x
        val y = localPoint.y
        val z = localPoint.z
        val sumSquared = x * x + y * y + z * z
        return normalise(Vector(
                4.0 * x * (sumSquared - paramSquared),
                4.0 * y * (sumSquared - paramSquared + 2.0 * rMaj * rMaj),
                4.0 * z * (sumSquared - paramSquared)
        ))
    }

    override fun equals(other: Any?) = other is Torus && closeTo(rMaj, other.rMaj) && closeTo(rMin, other.rMin) &&
            material == other.material && transform == other.transform
}
