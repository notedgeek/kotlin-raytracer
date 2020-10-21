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

interface SceneObjectCollector {

    fun addObject(obj: SceneObject)

    operator fun SceneObject.unaryPlus() {
        addObject(this)
    }

    fun sphere(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Sphere()).apply(block).obj

    fun plane(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Plane()).apply(block).obj

    fun cube(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Cube()).apply(block).obj

    fun cylinder(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Cylinder()).apply(block).obj

    fun cappedCylinder(block: ObjectBuilder.() -> Unit) =
        ObjectBuilder(Cylinder(cappedBottom = true, cappedTop = true)).apply(block).obj

    fun cone(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Cone()).apply(block).obj

    fun cappedCone(block: ObjectBuilder.() -> Unit) =
            ObjectBuilder(Cone(cappedBottom = true, cappedTop = true)).apply(block).obj

    fun triangle(p1: Point, p2: Point, p3: Point, block: ObjectBuilder.() -> Unit) =
        ObjectBuilder(Triangle(p1, p2, p3)).apply(block).obj

    fun boundingBox(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double,
                    block: ObjectBuilder.() -> Unit = {}) =
            ObjectBuilder(BoundingBox(Point(x1, y1, z1), Point(x2, y2, z2))).apply(block).obj

    fun group(group: Group = Group(), block: GroupBuilder.() -> Unit = {}) = GroupBuilder(group).apply(block).group

    fun union(left: SceneObject, right: SceneObject, block: ObjectBuilder.() -> Unit = {}) =
        ObjectBuilder(CSG(left, right, Operation.UNION)).apply(block).obj

    fun intersect(left: SceneObject, right: SceneObject, block: ObjectBuilder.() -> Unit = {}) =
        ObjectBuilder(CSG(left, right, Operation.INTERSECT)).apply(block).obj

    fun difference(left: SceneObject, right: SceneObject, block: ObjectBuilder.() -> Unit = {}) =
        ObjectBuilder(CSG(left, right, Operation.DIFFERENCE)).apply(block).obj

    fun from(obj: SceneObject, block: ObjectBuilder.() -> Unit) = ObjectBuilder(obj).apply(block).obj
}

interface Transformer {

    fun transform(transform: Matrix)

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

}


@SceneMarker
class SceneBuilder(scene: Scene) : SceneObjectCollector {

    private var camera = scene.camera
    private val lights = ArrayList<PointLight>()
    private val objects = ArrayList<SceneObject>()

    init {
        lights.addAll(scene.world.lights)
        objects.addAll(scene.world.objects)
    }

    override fun addObject(obj: SceneObject) {
        objects.add(obj)
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

    fun material(material: Material = Material(), block: MaterialBuilder.() -> Unit) =
            MaterialBuilder(material).apply(block).material


    fun toScene() = Scene(World(lights, objects), camera)

}

@SceneMarker
class ObjectBuilder(var obj: SceneObject) : Transformer {

    override fun transform(transform: Matrix) {
        obj = obj.transform(transform)
    }

    fun material(material: Material = obj.material, block: MaterialBuilder.() -> Unit = {}) {
        obj = obj.withMaterial(MaterialBuilder(material).apply(block).toMaterial())
    }

}

@SceneMarker
class GroupBuilder(var group: Group) : SceneObjectCollector, Transformer {

    override fun addObject(obj: SceneObject) {
        group = group.addChild(obj)
    }

    override fun transform(transform: Matrix) {
        group = group.transform(transform)
    }

    fun material(material: Material = Material(), block: MaterialBuilder.() -> Unit) {
        group = group.withMaterial(MaterialBuilder(material).apply(block).toMaterial())
    }

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

    fun ambient(ambient: Double) {
        material = material.withAmbient(ambient)
    }

    fun diffuse(diffuse: Double) {
        material = material.withDiffuse(diffuse)
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

fun main() {
    val scene = buildScene {

        +sphere {
            material {
                reflective(1.0)
            }
            rotateX(1.0)
        }

        val sphere1 = sphere {
            material {

            }
        }

        +sphere1

        +group {
            +sphere {  }
            +sphere1
        }

        val group2 = group {
            +cone {}
            +cylinder {}
        }

        +group2

        +union(
            sphere1,
            from(sphere1) {
                material {
                    colour(BLACK)
                }
            }
        )
    }

    println(scene)
}
