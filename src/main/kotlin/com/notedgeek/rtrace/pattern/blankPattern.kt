package com.notedgeek.rtrace.pattern

import com.notedgeek.rtrace.Colour
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.maths.Point

class BlankPattern(val colour: Colour) : Pattern() {

    override fun colourAt(point: Point) = colour

    override fun withTransform(transform: Matrix) = this

}