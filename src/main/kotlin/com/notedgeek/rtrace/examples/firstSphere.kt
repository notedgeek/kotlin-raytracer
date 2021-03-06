package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.Ray
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.normalise
import com.notedgeek.rtrace.obj.Sphere
import com.notedgeek.rtrace.graphics.PixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.hit
import com.notedgeek.rtrace.rotationZ
import com.notedgeek.rtrace.scaling
import java.awt.Color
import kotlin.math.PI

private class FirstSphere {

    val canvasPixels = 1000

    private val rayOrigin = Point(0, 0, -5)
    private val wallZ = 10.0
    private val wallSize = 7.0
    private val pixelSize = wallSize / canvasPixels
    private val shape = Sphere()
        .transform(scaling(1.0, 0.3, 1.0))
        .transform(rotationZ(PI / 4))
    private val half = wallSize / 2

    fun colorAt(x: Int, y: Int): Color {
        val worldX = -half + pixelSize * x
        val worldY = half - pixelSize * y
        val position = Point(worldX, worldY, wallZ)
        val r = Ray(rayOrigin, normalise(position - rayOrigin))
        val xs = shape.intersect(r)
        return if (hit(xs) != null) {
            Color.MAGENTA
        } else {
            Color.BLACK
        }
    }
}

fun main() {
    val sphere = FirstSphere()
    PixelSourceRenderer(PixelSource(sphere.canvasPixels, sphere.canvasPixels, sphere::colorAt))
}



