package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.Vector
import java.util.*

class Group(
        children: List<SceneObject> = emptyList(),
        private val boundingBox: BoundingBox? = null,
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null
) : SceneObject(material, transform, parent) {

    private val _children: MutableList<SceneObject> = LinkedList()

    init {
        for(child in children) {
            _children.add(child.withParent(this))
        }
    }

    fun addChild(child: SceneObject): Group {
        _children.add(child.withParent(this))
        return this
    }

    override fun withTransform(transform: Matrix): Group {
        return Group(_children, boundingBox, material, transform, parent)
    }

    override fun withMaterial(material: Material): Group {
        val newChildren = LinkedList<SceneObject>()
        for (child in _children) {
            newChildren.add(child.withMaterial(material))
        }
        return Group(newChildren, boundingBox, material, transform, parent)
    }

    override fun withParent(parent: SceneObject): Group {
        return Group(_children, boundingBox, material, transform, parent)
    }

    override fun includes(obj: SceneObject): Boolean {
        for (child in _children) {
            if (child.includes(obj)) {
                return true
            }
        }
        return false
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        var result = emptyList<Intersection>()
        for(child in _children) {
            result = result.addIntersections(child.intersect(localRay))
        }
        return result
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        throw Exception("localNormalAt should not be called on Groups")
    }

    fun split(threshold: Int = 4): Group {
        if(boundingBox == null || _children.size < threshold) {
            return this
        }
        //println("start bb: ${boundingBox.min} ${boundingBox.max} object count ${children.size}")
        val (leftBox, rightBox) = boundingBox.split()
        //println("left: ${leftBox.min} ${leftBox.max} right: ${rightBox.min} ${rightBox.max}")
        val leftChildren = LinkedList<SceneObject>()
        val rightChildren = LinkedList<SceneObject>()
        val orphans = LinkedList<SceneObject>()

        for(child in _children) {
            if(leftBox.containsObject(child)) {
                leftChildren.add(child)
            } else if(rightBox.containsObject(child)){
                rightChildren.add(child)
            } else orphans.add(child)
        }

        //println("left: ${leftChildren.size} right: ${rightChildren.size} orpahns: ${orphans.size}")

        val leftGroup = Group(leftChildren, leftBox, material, transform, parent).split()
        val rightGroup = Group(rightChildren, rightBox, material, transform, parent).split()
        val orphanGroup = Group(orphans, null, material, transform, parent)
        return Group(mutableListOf(leftGroup, rightGroup, orphanGroup), boundingBox, material, transform, parent)
    }

}
