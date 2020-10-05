package com.notedgeek.rtace.pattern

import com.notedgeek.rtace.Colour
import com.notedgeek.rtace.Matrix
import com.notedgeek.rtace.Point

class BlankPattern(val colour: Colour) : Pattern() {

    override fun colourAt(point: Point) = colour

    override fun withTransform(transform: Matrix) = this

}