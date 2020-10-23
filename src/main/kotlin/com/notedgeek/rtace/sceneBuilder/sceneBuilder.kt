package com.notedgeek.rtace.sceneBuilder

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.*
import com.notedgeek.rtace.pattern.BlankPattern
import com.notedgeek.rtace.pattern.Checkers
import com.notedgeek.rtace.pattern.Pattern
import com.notedgeek.rtace.pattern.Stripes
import java.util.*
import kotlin.collections.ArrayList

val EMPTY_SCENE = Scene(World(emptyList(), emptyList()), Camera())

fun buildScene(scene: Scene = EMPTY_SCENE,
               block: SceneBuilder.() -> Unit) = SceneBuilder(scene).apply(block).toScene()

fun buildObject(obj: SceneObject, block: ObjectBuilder.() -> Unit) = ObjectBuilder(obj).apply(block).obj

fun buildGroup(block: GroupBuilder.() -> Unit) = GroupBuilder().apply(block).toGroup()


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
        ObjectBuilder(Cylinder(cappedBottom = true, cappedTop = true, transform = translation(0.0, -0.5, 0.0)))
                .apply(block).obj

    fun cone(block: ObjectBuilder.() -> Unit) = ObjectBuilder(Cone()).apply(block).obj

    fun cappedCone(block: ObjectBuilder.() -> Unit) =
            ObjectBuilder(Cone(cappedBottom = true, cappedTop = true, transform = translation(0.0, 0.5, 0.0)))
                    .apply(block).obj

    fun triangle(p1: Point, p2: Point, p3: Point, block: ObjectBuilder.() -> Unit) =
        ObjectBuilder(Triangle(p1, p2, p3)).apply(block).obj

    fun group(block: GroupBuilder.() -> Unit = {}) = GroupBuilder().apply(block).toGroup()

    fun union(block: CsgBuilder.() -> Unit) = CsgBuilder(CSGOperation.UNION).apply(block).toCSG()

    fun difference(block: CsgBuilder.() -> Unit) = CsgBuilder(CSGOperation.DIFFERENCE).apply(block).toCSG()

    fun intersect(block: CsgBuilder.() -> Unit) = CsgBuilder(CSGOperation.INTERSECT).apply(block).toCSG()

    fun from(obj: SceneObject, block: ObjectBuilder.() -> Unit = {}) = ObjectBuilder(obj).apply(block).obj
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

    fun scaleY(y: Double) {
        transform(scaling(1.0, y, 1.0))
    }

    fun rotateX(r: Double) = transform(rotationX(r))

    fun rotateY(r: Double) = transform(rotationY(r))

    fun rotateZ(r: Double) = transform(rotationZ(r))

}

@SceneMarker
open class SceneBuilder(scene: Scene) : SceneObjectCollector {

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
open class GroupBuilder() : SceneObjectCollector, Transformer {

    val children = LinkedList<SceneObject>()
    var material = Material()
    var transform = I

    override fun addObject(obj: SceneObject) {
        children.add(obj)
    }

    override fun transform(transform: Matrix) {
        this.transform = transform * this.transform
    }

    fun material(material: Material = Material(), block: MaterialBuilder.() -> Unit) {
        this.material = MaterialBuilder(material).apply(block).toMaterial()
    }

    fun toGroup() = Group(children, material, transform).split(4)

}

@SceneMarker
class CsgBuilder(val operation: CSGOperation) : SceneObjectCollector, Transformer{

    var csg: CSG? = null
    var left: SceneObject? = null
    var material: Material? = null

    override fun addObject(obj: SceneObject) {
        val csg = this.csg
        val left = this.left
        if(csg != null) {
            this.csg = CSG(csg, obj, operation)
        } else if(left != null) {
            this.csg = CSG(left, obj, operation)
        } else {
            this.left = obj
        }
    }

    override fun transform(transform: Matrix) {
        val csg = this.csg ?: throw Exception("can't transform incomplete CSG")
        this.csg = csg.transform(transform) as CSG
    }

    fun toCSG(): CSG {
        val csg = this.csg
        val material = this.material
        if (csg == null) {
            throw Exception("cannot get incomplete CSG")
        }
        return if (material != null) {
            csg.withMaterial(material)
        } else {
            csg
        }
    }

    fun material(material: Material = Material(), block: MaterialBuilder.() -> Unit) {
        this.material = MaterialBuilder(material).apply(block).toMaterial()
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
