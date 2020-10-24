@file:Suppress("EqualsOrHashCode")

package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.I
import com.notedgeek.rtace.maths.Matrix
import com.notedgeek.rtace.maths.Point
import com.notedgeek.rtace.maths.Vector

class SmoothTriangle(
        p1: Point,
        p2: Point,
        p3: Point,
        val n1: Vector,
        val n2: Vector,
        val n3: Vector,
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null,
) : Triangle(p1, p2, p3, material, transform, parent) {

    override fun withTransform(transform: Matrix): SmoothTriangle {
        return SmoothTriangle(p1, p2, p3, n1, n2, n3,material, transform, parent)
    }

    override fun withMaterial(material: Material): SmoothTriangle {
        return SmoothTriangle(p1, p2, p3, n1, n2, n3, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): SmoothTriangle {
        return SmoothTriangle(p1, p2, p3, n1, n2, n3, material, transform, parent)
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection) =
            n2 * hit.u + n3 * hit.v + n1 * (1.0 - hit.u - hit.v)

    override fun equals(other: Any?) = other is SmoothTriangle && p1 == other.p1 && p2 == other.p2 && p3 == other.p3 &&
            n1 == other.n1 && n2 == other.n2 && n3 == other.n3 &&
            material == other.material && transform == other.transform

}