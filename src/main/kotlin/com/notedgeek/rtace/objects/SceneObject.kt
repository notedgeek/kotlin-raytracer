package com.notedgeek.rtace.objects

import com.notedgeek.rtace.*

abstract class SceneObject(
    val material: Material = Material(),
    val transform: Matrix = I
) {

    val inverseTransform = -transform

    abstract fun intersects(r: Ray): List<Intersection>

    abstract fun withTransform(transform: Matrix): SceneObject

    abstract fun withMaterial(material: Material): SceneObject

    abstract fun normalAt(worldPoint: Point): Vector

    fun withColour(r: Double, g: Double, b: Double) = withColour(colour(r, g, b))

    fun withColour(colour: Colour) =
        withMaterial(material(colour, material.ambient, material.diffuse, material.specular, material.shininess))

    fun withAmbient(ambient: Double) =
        withMaterial(material(material.colour, ambient, material.diffuse, material.specular, material.shininess))

    fun withDiffuse(diffuse: Double) =
        withMaterial(material(material.colour, material.ambient, diffuse, material.specular, material.shininess))

    fun withSpecular(specular: Double) =
        withMaterial(material(material.colour, material.ambient, material.diffuse, specular, material.shininess))

    fun withShininess(shininess: Double) =
        withMaterial(material(material.colour, material.ambient, material.diffuse, material.specular, shininess))

    fun transform(transform: Matrix): SceneObject {
        return withTransform(transform * this.transform)
    }
    
    fun translate(x: Double, y: Double, z: Double) = transform(translation(x, y, z))
    
    fun translateX(x: Double) = transform(translation(x, 0.0, 0.0))
    
    fun translateY(y: Double) = transform(translation(0.0, y, 0.0))
    
    fun translateZ(z: Double) = transform(translation(0.0, 0.0, z))
    
    fun scale(x: Double, y: Double, z: Double) = transform(scaling(x, y, z))
    
    fun scaleX(x: Double) = transform(scaling(x, 0.0, 0.0))
    
    fun scaleY(y: Double) = transform(scaling(0.0, y, 0.0))
    
    fun scaleZ(z: Double) = transform(scaling(0.0, 0.0, z))
    
    fun rotateX(r: Double) = transform(rotationX(r))

    fun rotateY(r: Double) = transform(rotationY(r))

    fun rotateZ(r: Double) = transform(rotationZ(r))
}
