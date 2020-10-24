package com.notedgeek.rtace.pattern

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.EPSILON
import com.notedgeek.rtace.maths.I
import com.notedgeek.rtace.maths.Matrix
import com.notedgeek.rtace.maths.Point
import kotlin.math.floor

class Checkers(private val colour1: Colour, private val colour2: Colour, transform: Matrix = I) : Pattern(transform) {

    override fun colourAt(point: Point) = if ((floor(point.x + EPSILON) + floor(point.y + EPSILON) + floor(point.z + EPSILON)).toInt() % 2 == 0)
        colour1 else colour2

    override fun withTransform(transform: Matrix) = Checkers(colour1, colour2, transform)

}