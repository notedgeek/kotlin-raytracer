package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.*
import com.notedgeek.rtace.pattern.BlankPattern

abstract class SceneObject(
        val material: Material = Material(),
        val transform: Matrix = I,
        val parent: SceneObject? = null,
        var bounds: BoundingBox = BoundingBox(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
) {

    val inverseTransform = if (transform === I) I else -transform
    private val parentSpaceBounds = bounds.transform(transform)

    fun intersect(ray: Ray): List<Intersection> = localIntersect(ray.transform(inverseTransform))

    open fun bounds(): BoundingBox = bounds

    open fun parentSpaceBounds() = parentSpaceBounds

    fun normalAt(worldPoint: Point, hit: Intersection): Vector {
        val localPoint = worldToObject(worldPoint)
        val localNormal = localNormalAt(localPoint, hit)
        return normalToWorld(localNormal)
    }

    private fun worldToObject(point: Point): Point {
        var p = point
        if(parent != null) {
            p = parent.worldToObject(p)
        }
        return inverseTransform * p
    }

    private fun normalToWorld(normal: Vector): Vector {
        var n = normalise(transpose(inverseTransform) * normal)
        if(parent != null) {
            n = parent.normalToWorld(n)
        }
        return n
    }

    fun colour(r: Double, g: Double, b: Double) = colour(Colour(r, g, b))

    fun colour(colour: Colour) =
        withMaterial(Material(colour, BlankPattern(colour), material.ambient, material.diffuse,
            material.specular, material.shininess, material.reflective))

    fun ambient(ambient: Double) =
        withMaterial(Material(material.colour, material.pattern, ambient, material.diffuse,
            material.specular, material.shininess, material.reflective))

    fun diffuse(diffuse: Double) =
        withMaterial(Material(material.colour, material.pattern, material.ambient,
            diffuse, material.specular, material.shininess, material.reflective))

    fun specular(specular: Double) =
        withMaterial(Material(material.colour, material.pattern,material.ambient,
            material.diffuse, specular, material.shininess, material.reflective))

    fun reflective(reflective: Double) =
        withMaterial(Material(material.colour, material.pattern,material.ambient,
            material.diffuse, material.specular, material.shininess, reflective))

    fun transform(transform: Matrix): SceneObject {
        return withTransform(transform * this.transform)
    }

    open fun includes(obj: SceneObject) = this == obj

    fun translate(x: Double, y: Double, z: Double) = transform(translation(x, y, z))

    fun translateX(x: Double) = transform(translation(x, 0.0, 0.0))

    fun translateY(y: Double) = transform(translation(0.0, y, 0.0))

    fun translateZ(z: Double) = transform(translation(0.0, 0.0, z))

    fun scale(x: Double, y: Double, z: Double) = transform(scaling(x, y, z))

    fun rotateX(r: Double) = transform(rotationX(r))

    fun rotateY(r: Double) = transform(rotationY(r))

    fun rotateZ(r: Double) = transform(rotationZ(r))

    abstract fun localIntersect(localRay: Ray): List<Intersection>

    abstract fun withTransform(transform: Matrix): SceneObject

    abstract fun withMaterial(material: Material): SceneObject

    abstract fun withParent(parent: SceneObject): SceneObject

    abstract fun localNormalAt(localPoint: Point, hit: Intersection): Vector

}
