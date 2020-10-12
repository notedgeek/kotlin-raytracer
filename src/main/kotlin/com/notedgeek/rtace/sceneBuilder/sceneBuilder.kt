package com.notedgeek.rtace.sceneBuilder

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.Plane
import com.notedgeek.rtace.obj.SceneObject
import com.notedgeek.rtace.obj.Sphere
import com.notedgeek.rtace.pattern.BlankPattern
import com.notedgeek.rtace.pattern.Checkers
import com.notedgeek.rtace.pattern.Pattern
import com.notedgeek.rtace.pattern.Stripes
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

fun makeScene(block: SceneBuilder.() -> Unit) = SceneBuilder().apply(block).toScene()

class SceneBuilder {

    private var width = 500
    private var height = 250
    private var fov = PI / 3
    private var viewPoint = Point(0, 1, -5)
    private var lookAt = Point(0, 0, 0)
    private var upVector = Vector(0, 1, 0)

    private val lights = ArrayList<PointLight>()
    private val objects = ArrayList<SceneObject>()

    fun size(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun viewPoint(x: Double, y: Double, z:Double) {
        viewPoint = Point(x, y, z)
    }

    fun lookAt(x: Double, y: Double, z:Double) {
        lookAt = Point(x, y, z)
    }

    fun light(block: LightBuilder.() -> Unit) {
        lights.add(LightBuilder().apply(block).toLight())
    }

    fun sphere(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Sphere()).apply(block).toObject())
    }

    fun plane(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Plane()).apply(block).toObject())
    }

    fun material(block: MaterialBuilder.() -> Unit) = MaterialBuilder().apply(block).toMaterial()

    fun def(block: ObjectDefiner.() -> Unit) = ObjectDefiner().apply(block).toObject()

    fun add(obj: SceneObject, block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(obj).apply(block).toObject())
    }

    fun toScene() = Scene(World(lights, objects),
        Camera(width, height, fov, viewTransformation(viewPoint, lookAt, upVector)))

}

class ObjectDefiner {

    private var obj:SceneObject = Sphere()

    fun sphere(block: ObjectBuilder.() -> Unit) {
        obj = ObjectBuilder(Sphere()).apply(block).toObject()
    }

    fun toObject() = obj
}

class ObjectBuilder(var obj: SceneObject){

    fun translate(x: Double, y: Double, z: Double) = transform(translation(x, y, z))

    fun translateX(x: Double) = transform(translation(x, 0.0, 0.0))

    fun translateY(y: Double) = transform(translation(0.0, y, 0.0))

    fun translateZ(z: Double) = transform(translation(0.0, 0.0, z))

    fun scale(s: Double) = scale(s, s, s)

    fun scale(x: Double, y: Double, z: Double) {
        transform(scaling(x, y, z))
    }

    fun rotateX(r: Double) = transform(rotationX(r))

    fun rotateY(r: Double) = transform(rotationY(r))

    fun rotateZ(r: Double) = transform(rotationZ(r))

    fun transform(transform: Matrix) {
        obj = obj.withTransform(transform * obj.transform)
    }

    fun material(material: Material) {
        obj = obj.withMaterial(material)
    }

    fun material(block: MaterialBuilder.() -> Unit) {
        material(MaterialBuilder().apply(block).toMaterial())
    }

    fun toObject() = obj

}

class LightBuilder {

    private var point = Point(-2, 2, -2)
    private var intensity = WHITE

    fun at(x: Double, y: Double, z: Double) {
        point = Point(x, y, z)
    }

    fun toLight() = PointLight(point, intensity)
}

class MaterialBuilder {

    private var material = Material()

    fun pattern(block: PatternBuilder.() -> Unit) {
        material = material.withPattern(PatternBuilder().apply(block).toPattern())
    }

    fun diffuse(diffuse: Double) {
        material = material.withDiffuse(diffuse)
    }

    fun specular(specular: Double) {
        material = material.withSpecular(specular)
    }

    fun colour(r: Double, g: Double, b: Double) {
        material = material.withColour(Colour(r, g, b))
    }

    fun toMaterial() = material

}

class PatternBuilder {

    private var pattern: Pattern = BlankPattern(WHITE)

    fun checkers(c1: Colour, c2: Colour) {
        pattern = Checkers(c1, c2)
    }

    fun stripes(c1: Colour, c2: Colour) {
        pattern = Stripes(c1, c2)
    }

    fun scale(s: Double) {
        scale(s, s, s)
    }

    fun scale(x: Double, y: Double, z: Double) {
        transform(scaling(x, y, z))
    }

    fun rotateX(r: Double) {
        transform(rotationY(r))
    }

    fun rotateY(r: Double) {
        transform(rotationY(r))
    }

    fun rotateZ(r: Double) {
        transform(rotationZ(r))
    }

    fun toPattern() = pattern

    fun colour(r: Double, g: Double, b: Double) = Colour(r, g, b)

    private fun transform(transform: Matrix) {
        pattern = pattern.transform(transform)
    }
}

