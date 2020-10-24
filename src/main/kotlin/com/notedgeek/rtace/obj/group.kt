package com.notedgeek.rtace.obj

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.I
import com.notedgeek.rtace.maths.Matrix
import com.notedgeek.rtace.maths.Point
import com.notedgeek.rtace.maths.Vector
import java.util.*

class Group(
        children: List<SceneObject> = emptyList(),
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null
) : SceneObject(material, transform, parent) {

    private val _children: MutableList<SceneObject> = LinkedList()

    private val parentSpaceBounds: BoundingBox

    init {
        for(child in children) {
            _children.add(child.withParent(this))
        }
        bounds = calculateBounds()

        parentSpaceBounds = bounds.transform(transform)
    }

    private fun calculateBounds(): BoundingBox {
        var result = BoundingBox()
        for(child in _children) {
            result = result.addBoundingBox(child.parentSpaceBounds())
        }
        return result
    }

    override fun bounds() = bounds

    override fun parentSpaceBounds() = parentSpaceBounds

    override fun withTransform(transform: Matrix) = Group(_children, material, transform, parent)

    override fun withMaterial(material: Material): Group {
        val newChildren = LinkedList<SceneObject>()
        for (child in _children) {
            newChildren.add(child.withMaterial(material))
        }
        return Group(newChildren, material, transform, parent)
    }

    override fun withParent(parent: SceneObject) = Group(_children, material, transform, parent)

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
        if(!bounds().hitBy(localRay)) {
            return result
        }
        for(child in _children) {
            result = result.addIntersections(child.intersect(localRay))
        }
        return result
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        throw Exception("localNormalAt should not be called on Groups")
    }

    fun split(threshold: Int = 5): Group {
        if(_children.size < threshold) {
            return this
        }
        //println("start bb: ${boundingBox.min} ${boundingBox.max} object count ${children.size}")
        val (leftBox, rightBox) = bounds.split()
        //println("left: ${leftBox.min} ${leftBox.max} right: ${rightBox.min} ${rightBox.max}")
        val leftChildren = LinkedList<SceneObject>()
        val rightChildren = LinkedList<SceneObject>()
        val orphans = LinkedList<SceneObject>()

        for(child in _children) {
            when {
                leftBox.containsObject(child) -> {
                    leftChildren.add(child)
                }
                rightBox.containsObject(child) -> {
                    rightChildren.add(child)
                }
                else -> orphans.add(child)
            }
        }

        //println("left: ${leftChildren.size} right: ${rightChildren.size} orphans: ${orphans.size}")

        val listToAdd = LinkedList<SceneObject>()
        if(!leftChildren.isEmpty()) {
            listToAdd.add(Group(leftChildren, material, transform, parent).split(threshold))
        }
        if(!rightChildren.isEmpty()) {
            listToAdd.add(Group(rightChildren, material, transform, parent).split(threshold))
        }
        if(!orphans.isEmpty()) {
            listToAdd.add(Group(orphans, material, transform, parent))
        }
        return Group(listToAdd, material, transform, parent)
    }

}
