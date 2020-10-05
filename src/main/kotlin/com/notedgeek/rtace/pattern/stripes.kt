package com.notedgeek.rtace.pattern

import com.notedgeek.rtace.*
import kotlin.math.floor

class Stripes(val colour1: Colour, val colour2: Colour, transform: Matrix = I) : Pattern(transform) {

    override fun colourAt(point: Point) = if (floor(point.x + EPSILON / 2.0).toInt() % 2 == 0) colour1 else colour2

    override fun withTransform(transform: Matrix) = Stripes(colour1, colour2, transform)

}