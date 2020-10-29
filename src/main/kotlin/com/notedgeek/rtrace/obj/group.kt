package com.notedgeek.rtrace.obj

import com.notedgeek.rtrace.Intersection
import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.addIntersections
import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.Vector
import java.util.*

open class Group(
        var children: List<SceneObject> = emptyList(),
        material: Material = Material(),
        transform: Matrix = I,
        parent: SceneObject? = null
) : SceneObject(material, transform, parent) {

    private val parentSpaceBounds: BoundingBox

    init {
        val children = LinkedList<SceneObject>()
        for (child in this.children) {
            children.add(child.withParent(this))
        }
        this.children = children
        bounds = calculateBounds()
        parentSpaceBounds = bounds.transform(transform)
    }

    private fun calculateBounds(): BoundingBox {
        var result = BoundingBox()
        for (child in children) {
            result = result.addBoundingBox(child.parentSpaceBounds())
        }
        return result
    }

    override fun bounds() = bounds

    override fun parentSpaceBounds() = parentSpaceBounds

    override fun withTransform(transform: Matrix) = Group(children, material, transform, parent)

    override fun withMaterial(material: Material): Group {
        val newChildren = LinkedList<SceneObject>()
        for (child in children) {
            newChildren.add(child.withMaterial(material))
        }
        return Group(newChildren, material, transform, parent)
    }

    override fun withParent(parent: SceneObject) = Group(children, material, transform, parent)

    override fun includes(obj: SceneObject): Boolean {
        for (child in children) {
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
        for (child in children) {
            result = result.addIntersections(child.intersect(localRay))
        }
        return result
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection): Vector {
        throw Exception("localNormalAt should not be called on Groups")
    }

    fun split(threshold: Int = 5): Group {
        if (children.size < threshold) {
            return this
        }
        //println("start bb: ${boundingBox.min} ${boundingBox.max} object count ${children.size}")
        val (leftBox, rightBox) = bounds.split()
        //println("left: ${leftBox.min} ${leftBox.max} right: ${rightBox.min} ${rightBox.max}")
        val leftChildren = LinkedList<SceneObject>()
        val rightChildren = LinkedList<SceneObject>()
        val orphans = LinkedList<SceneObject>()

        for (child in children) {
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
            listToAdd.add(Group(leftChildren, material, I, parent).split(threshold))
        }
        if(!rightChildren.isEmpty()) {
            listToAdd.add(Group(rightChildren, material, I, parent).split(threshold))
        }
        if(!orphans.isEmpty()) {
            listToAdd.add(Group(orphans, material, I, parent))
        }
        return Group(listToAdd, material, transform, parent)
    }

}
