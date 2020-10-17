package com.notedgeek.rtace.sceneBuilder

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.*
import com.notedgeek.rtace.pattern.BlankPattern
import com.notedgeek.rtace.pattern.Checkers
import com.notedgeek.rtace.pattern.Pattern
import com.notedgeek.rtace.pattern.Stripes

fun buildScene(scene: Scene = Scene(World(emptyList(), emptyList()), Camera()),
               block: SceneBuilder.() -> Unit) = SceneBuilder(scene).apply(block).toScene()

@DslMarker
annotation class SceneMarker

@SceneMarker
class SceneBuilder(scene: Scene) {

    private var camera = scene.camera
    private val lights = ArrayList<PointLight>()
    private val objects = ArrayList<SceneObject>()

    init {
        lights.addAll(scene.world.lights)
        objects.addAll(scene.world.objects)
    }

    fun size(width: Int, height: Int) {
        camera = camera.withSize(width, height)
    }

    fun viewPoint(x: Double, y: Double, z:Double) {
        camera = camera.withFrom(Point(x, y, z))
    }

    fun lookAt(x: Double, y: Double, z:Double) {
        camera = camera.withTo(Point(x, y, z))
    }

    fun pointLight(block: LightBuilder.() -> Unit) {
        lights.add(LightBuilder().apply(block).toLight())
    }

    fun sphere(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Sphere()).apply(block).toObject())
    }

    fun plane(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Plane()).apply(block).toObject())
    }

    fun cube(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Cube()).apply(block).toObject())
    }

    fun cylinder(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Cylinder()).apply(block).toObject())
    }

    fun cappedCylinder(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Cylinder(cappedBottom = true, cappedTop = true)).apply(block).toObject())
    }

    fun cone(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Cone()).apply(block).toObject())
    }

    fun cappedCone(block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(Cone(cappedBottom = true, cappedTop = true)).apply(block).toObject())
    }

    fun material(block: MaterialBuilder.() -> Unit) = MaterialBuilder().apply(block).toMaterial()

    fun def(block: ObjectDefiner.() -> Unit) = ObjectDefiner().apply(block).toObject()

    fun add(obj: SceneObject = Sphere(), block: ObjectBuilder.() -> Unit) {
        objects.add(ObjectBuilder(obj).apply(block).toObject())
    }

    fun toScene() = Scene(World(lights, objects), camera)

}

@SceneMarker
class ObjectDefiner {

    private var obj:SceneObject = Sphere()

    fun sphere(block: ObjectBuilder.() -> Unit) {
        obj = ObjectBuilder(Sphere()).apply(block).toObject()
    }

    fun cube(block: ObjectBuilder.() -> Unit) {
        obj = ObjectBuilder(Cube()).apply(block).toObject()
    }

    fun toObject() = obj
}

@SceneMarker
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
        material(MaterialBuilder(obj.material).apply(block).toMaterial())
    }

    fun toObject() = obj

}

@SceneMarker
class LightBuilder {

    private var point = Point(-2, 2, -2)
    private var intensity = WHITE

    fun at(x: Double, y: Double, z: Double) {
        point = Point(x, y, z)
    }

    fun toLight() = PointLight(point, intensity)
}

@SceneMarker
class MaterialBuilder(var material: Material = Material()) {

    fun pattern(block: PatternBuilder.() -> Unit) {
        material = material.withPattern(PatternBuilder().apply(block).toPattern())
    }

    fun diffuse(diffuse: Double) {
        material = material.withDiffuse(diffuse)
    }

    fun ambient(ambient: Double) {
        material = material.withAmbient(ambient)
    }

    fun specular(specular: Double) {
        material = material.withSpecular(specular)
    }

    fun shininess(shininess: Double) {
        material = material.withShininess(shininess)
    }

    fun reflective(reflective: Double) {
        material = material.withReflective(reflective)
    }

    fun transparency(transparency: Double) {
        material = material.withTransparency(transparency)
    }

    fun refractiveIndex(refractiveIndex: Double) {
        material = material.withRefractiveIndex(refractiveIndex)
    }

    fun colour(c: Colour) = colour(c.red, c.green, c.blue)

    fun colour(r: Double, g: Double, b: Double) {
        material = material.withColour(Colour(r, g, b))
    }

    fun toMaterial() = material

}

@SceneMarker
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

    fun rotateZ(r: Double) {
        transform(rotationZ(r))
    }

    fun toPattern() = pattern

    fun colour(r: Double, g: Double, b: Double) = Colour(r, g, b)

    private fun transform(transform: Matrix) {
        pattern = pattern.transform(transform)
    }
}

