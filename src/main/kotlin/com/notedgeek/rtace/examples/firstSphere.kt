package com.notedgeek.rtace.examples

import com.notedgeek.rtace.*
import com.notedgeek.rtace.`object`.sphere
import com.notedgeek.rtrace.graphics.pixelSource
import com.notedgeek.rtrace.graphics.pixelSourceRenderer
import java.awt.Color
import kotlin.math.PI

private class FirstSphere {

    val canvasPixels = 1000

    private val rayOrigin = point(0, 0, -5)
    private val wallZ = 10.0
    private val wallSize = 7.0
    private val pixelSize = wallSize / canvasPixels
    private val shape = sphere()
        .transform(scaling(1.0, 0.3, 1.0))
        .transform(rotationZ(PI / 4))
    private val half = wallSize / 2

    fun colorAt(x: Int, y: Int): Color {
        val worldX = -half + pixelSize * x
        val worldY = half - pixelSize * y
        val position = point(worldX, worldY, wallZ)
        val r = ray(rayOrigin, normalise(position - rayOrigin))
        val xs = shape.intersects(r)
        return if (hit(xs) != null) {
            Color.MAGENTA
        } else {
            Color.BLACK
        }
    }
}

fun main() {
    val sphere = FirstSphere()
    pixelSourceRenderer(pixelSource(sphere.canvasPixels, sphere.canvasPixels, sphere::colorAt))
}



