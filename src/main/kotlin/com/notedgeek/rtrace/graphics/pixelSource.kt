package com.notedgeek.rtrace.graphics

import java.awt.Color

class pixelSource(var width: Int, var height: Int, var ca: (x: Int, y: Int) -> Color) {
    fun colorAt(x: Int, y: Int) = ca(x, y)
}