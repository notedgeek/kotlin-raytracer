package com.notedgeek.rtace.examples

import com.notedgeek.rtace.*
import com.notedgeek.rtrace.graphics.PixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import java.awt.Color
import kotlin.math.PI

val rayOrigin = point(0, 0, -5)
const val wallZ = 10.0
const val wallSize = 7.0
const val canvasPixels = 1000
const val pixelSize = wallSize / canvasPixels
val shape = sphere()
    .transform(scaling(1.0, 0.3, 1.0))
    .transform(rotationZ(PI / 4))

const val half = wallSize / 2

fun colorAt(x: Int, y: Int): Color {
    val worldX = -half + pixelSize * x
    val worldY = half - pixelSize * y
    val position = point(worldX, worldY, wallZ)
    val r = ray(rayOrigin, normalise(position - rayOrigin))
    val xs = shape.intersects(r)
    return if(hit(xs) != null) {
        Color.MAGENTA
    } else {
        Color.BLACK
    }
}

fun main() {
    PixelSourceRenderer(PixelSource(canvasPixels, canvasPixels, ::colorAt))
}


