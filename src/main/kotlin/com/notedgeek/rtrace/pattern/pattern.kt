package com.notedgeek.rtrace.pattern

import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.obj.SceneObject
import com.notedgeek.rtrace.Colour
import com.notedgeek.rtrace.rotationY
import com.notedgeek.rtrace.scaling

abstract class Pattern(val transform: Matrix = I) {

    private val inverseTransform = -transform

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