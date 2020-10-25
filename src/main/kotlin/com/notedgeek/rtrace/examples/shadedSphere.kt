package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.*
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.maths.normalise
import com.notedgeek.rtrace.obj.Sphere
import com.notedgeek.rtrace.graphics.PixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import java.awt.Color

private class ShadedSphere {

    val canvasPixels = 1200

    private val rayOrigin = Point(0, 0, -5)
    private val wallZ = 10.0
    private val wallSize = 7.0
    private val pixelSize = wallSize / canvasPixels
    private val shape = Sphere().withMaterial(Material(Colour(1.0, 0.2, 1.0)))
    private val light = PointLight(Point(-10, 10, -10), WHITE)
    private val half = wallSize / 2

    fun colorAt(x: Int, y: Int): Color {
        val worldX = -half + pixelSize * x
        val worldY = half - pixelSize * y
        val position = Point(worldX, worldY, wallZ)
        val r = Ray(rayOrigin, normalise(position - rayOrigin))
        val xs = shape.localIntersect(r)
        val hit = hit(xs)
        return if (hit != null) {
            val point = position(r, hit.t)
            val o = hit.obj
            val normal = o.normalAt(point, hit)
            val eyeV = - r.direction
            surfaceColour(o.material, light, point, eyeV, normal, o).toAWT()
        } else {
            BLACK.toAWT()
        }
    }
}

fun main() {
    val sphere = ShadedSphere()
    PixelSourceRenderer(PixelSource(sphere.canvasPixels, sphere.canvasPixels, sphere::colorAt))
}
