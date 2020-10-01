package com.notedgeek.rtrace.graphics

interface renderContext {
    val sizeX: Int
    val sizeY: Int
    fun setRgb(x: Int, y: Int, r: Int, g: Int, b: Int)
}