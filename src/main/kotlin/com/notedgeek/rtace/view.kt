package com.notedgeek.rtace

import com.notedgeek.rtrace.graphics.PixelSource
import kotlin.math.tan

fun viewTransformation(from: Point, to: Point, up: Vector = Vector(0, 1, 0)): Matrix {
    val forward = normalise(to - from)
    val upn = normalise(up)
    val left = forward cross upn
    val trueUp = left cross forward
    val orientation = Matrix(
        left.x,     left.y,     left.z,     0.0,
        trueUp.x,   trueUp.y,   trueUp.z,   0.0,
        -forward.x, -forward.y, -forward.z, 0.0,
        0.0,        0.0,        0.0,        1.0
    )
    return orientation * translation(-from.x, -from.y, -from.z)
}

class Camera(val width: Int, val height: Int, val fov: Double,
             val transformation: Matrix = I) {

    val pixelSize: Double

    private val halfView = tan(fov / 2)
    private val aspect = width.toDouble() / height
    private val halfWidth: Double
    private val halfHeight: Double
    private val inverseTransformation = - transformation

    init {
        if (aspect >= 1.0) {
            halfWidth = halfView
            halfHeight = halfView / aspect
        } else {
            halfWidth = halfView * aspect
            halfHeight = halfView
        }
        pixelSize = halfWidth * 2 / width
    }

    fun rayForPixel(px: Int, py: Int): Ray {
        val xOffset = (px + 0.5) * pixelSize
        val yOffset = (py + 0.5) * pixelSize
        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset
        val pixel = inverseTransformation * Point(worldX, worldY, -1.0)
        val origin = inverseTransformation * Point(0, 0, 0)
        val direction = normalise(pixel - origin)
        return Ray(origin, direction)
    }

    override fun toString(): String {
        return "Camera(width=$width, height=$height, fov=$fov, transformation=$transformation)"
    }
}

fun pixelSource(scene: Scene) =
    PixelSource(scene.camera.width, scene.camera.height) {
            x, y -> scene.world.colourAt(scene.camera.rayForPixel(x, y)).toAWT()
    }
