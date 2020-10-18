package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.Vector
import java.util.*

class Group(
    material: Material = Material(),
    transformation: Matrix = I,
    parent: SceneObject? = null,
    private val children: MutableList<SceneObject> = LinkedList()
) : SceneObject(material, transformation, parent) {

    fun addChild(child: SceneObject): Group {
        children.add(child.transform(transform))
        return this
    }

    override fun transform(transform: Matrix): Group {
        val newList = LinkedList<SceneObject>()
        for (child in children) {
            newList.add(child.transform(transform))
        }
        return Group(material, transform * this.transform, parent, newList)
    }


    override fun withTransform(transform: Matrix): Group {
        throw Exception("withTransform should not be called on Groups")
    }

    override fun withMaterial(material: Material): Group {
        val newList = LinkedList<SceneObject>()
        for (child in children) {
            newList.add(child.withMaterial(material))
        }
        return Group(material, transform, parent, newList)
    }

    override fun withParent(parent: SceneObject): Group {
        return Group(material, transform, parent, children)
    }

    override fun intersect(ray: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        for(child in children) {
            val intersections = child.intersect(ray)
            if (intersections.isNotEmpty()) {
                result = result.addIntersections(intersections)
            }
        }
        return result
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        throw Exception("localIntersect should not be called on Groups")
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        throw Exception("localNormalAt should not be called on Groups")
    }

}
