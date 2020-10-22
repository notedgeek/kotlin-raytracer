package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.Vector
import java.util.*

class Group(
        private val children: MutableList<SceneObject> = LinkedList(),
        private val boundingBox: BoundingBox? = null,
        material: Material = Material(),
        parent: SceneObject? = null
) : SceneObject(material, I, parent) {

    fun addChild(child: SceneObject): Group {
        children.add(child.transform(transform))
        return this
    }

    override fun transform(transform: Matrix): Group {
        val newChildren = LinkedList<SceneObject>()
        for (child in children) {
            newChildren.add(child.transform(transform))
        }
        val newBoundingBox = if(boundingBox == null) null else
            BoundingBox(boundingBox.min, boundingBox.max, transform * boundingBox.transform)
        return Group(newChildren, newBoundingBox, material, parent)
    }


    override fun withTransform(transform: Matrix): Group {
        throw Exception("withTransform should not be called on Groups")
    }

    override fun withMaterial(material: Material): Group {
        val newChildren = LinkedList<SceneObject>()
        for (child in children) {
            newChildren.add(child.withMaterial(material))
        }
        return Group(newChildren, boundingBox, material, parent)
    }

    override fun withParent(parent: SceneObject): Group {
        return Group(children, boundingBox, material, parent)
    }

    override fun intersect(ray: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        if(boundingBox != null && !boundingBox.hitBy(ray)) {
            return result
        }
        for(child in children) {
            val intersections = child.intersect(ray)
            if (intersections.isNotEmpty()) {
                result = result.addIntersections(intersections)
            }
        }
        return result
    }

    override fun includes(obj: SceneObject): Boolean {
        for (child in children) {
            if (child.includes(obj)) {
                return true
            }
        }
        return false
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        throw Exception("localIntersect should not be called on Groups")
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        throw Exception("localNormalAt should not be called on Groups")
    }

    fun split(threshhold: Int = 4): Group {
        if(boundingBox == null || children.size < threshhold) {
            return this
        }
        //println("start bb: ${boundingBox.min} ${boundingBox.max} object count ${children.size}")
        val (leftBox, rightBox) = boundingBox.split()
        //println("left: ${leftBox.min} ${leftBox.max} right: ${rightBox.min} ${rightBox.max}")
        val leftChildren = LinkedList<SceneObject>()
        val rightChildren = LinkedList<SceneObject>()
        val orphans = LinkedList<SceneObject>()

        for(child in children) {
            if(leftBox.containsObject(child)) {
                leftChildren.add(child)
            } else if(rightBox.containsObject(child)){
                rightChildren.add(child)
            } else orphans.add(child)
        }

        //println("left: ${leftChildren.size} right: ${rightChildren.size} orpahns: ${orphans.size}")

        val leftGroup = Group(leftChildren, leftBox, material, parent).split()
        val rightGroup = Group(rightChildren, rightBox, material, parent).split()
        val orphanGroup = Group(orphans, null, material, parent)
        return Group(mutableListOf(leftGroup, rightGroup, orphanGroup), boundingBox, material, parent)
    }

}
