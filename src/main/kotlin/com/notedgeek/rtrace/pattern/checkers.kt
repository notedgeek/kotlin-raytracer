package com.notedgeek.rtrace.pattern

import com.notedgeek.rtrace.maths.EPSILON
import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.Colour
import kotlin.math.floor

class Checkers(private val colour1: Colour, private val colour2: Colour, transform: Matrix = I) : Pattern(transform) {

    override fun colourAt(point: Point) = if ((floor(point.x + EPSILON) + floor(point.y + EPSILON) + floor(point.z + EPSILON)).toInt() % 2 == 0)
        colour1 else colour2

    override fun withTransform(transform: Matrix) = Checkers(colour1, colour2, transform)

}