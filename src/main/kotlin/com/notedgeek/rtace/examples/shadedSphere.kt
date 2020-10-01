package com.notedgeek.rtace.examples

import com.notedgeek.rtace.*
import com.notedgeek.rtace.`object`.sphere
import com.notedgeek.rtrace.graphics.pixelSource
import com.notedgeek.rtrace.graphics.pixelSourceRenderer
import java.awt.Color

private class ShadedSphere {

    val canvasPixels = 1200

    private val rayOrigin = point(0, 0, -5)
    private val wallZ = 10.0
    private val wallSize = 7.0
    private val pixelSize = wallSize / canvasPixels
    private val shape = sphere().withMaterial(material(colour(1.0, 0.2, 1.0)))
    private val light = pointLight(point(-10, 10, -10), WHITE)
    private val half = wallSize / 2

    fun colorAt(x: Int, y: Int): Color {
        val worldX = -half + pixelSize * x
        val worldY = half - pixelSize * y
        val position = point(worldX, worldY, wallZ)
        val r = ray(rayOrigin, normalise(position - rayOrigin))
        val xs = shape.intersects(r)
        val hit = hit(xs)
        return if (hit != null) {
            val point = position(r, hit.t)
            val o = hit.obj
            val normal = o.normalAt(point)
            val eyeV = - r.direction
            lighting(o.material, light, point, eyeV, normal).toAWT()
        } else {
            BLACK.toAWT()
        }
    }
}

fun main() {
    val sphere = ShadedSphere()
    pixelSourceRenderer(pixelSource(sphere.canvasPixels, sphere.canvasPixels, sphere::colorAt))
}
