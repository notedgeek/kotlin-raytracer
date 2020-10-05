package com.notedgeek.rtace.pattern

import com.notedgeek.rtace.*
import com.notedgeek.rtace.obj.SceneObject

abstract class Pattern(val transform: Matrix = I) {

    val inverseTransform = -transform

    fun colourAtObject(obj: SceneObject, worldPoint: Point): Colour {
        val objectPoint = obj.inverseTransform * worldPoint
        val patternPoint = inverseTransform * objectPoint
        return colourAt(patternPoint)
    }

    fun transform(transform: Matrix): Pattern {
        return withTransform(transform * this.transform)
    }

    fun scale(x: Double, y: Double, z: Double) = transform(scaling(x, y, z))

    fun rotateY(r: Double) = transform(rotationY(r))

    abstract fun colourAt(point: Point): Colour

    abstract fun withTransform(transform: Matrix): Pattern

}