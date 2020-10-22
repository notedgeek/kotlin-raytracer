package com.notedgeek.rtace

import com.notedgeek.rtrace.graphics.PixelSource
import kotlin.math.PI
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

class Camera(val width: Int = 1000, val height: Int = 500, val fov: Double = PI / 3,
             private val from: Point = Point(0, 0, 0), private val to: Point = Point(0, 0, -1),
             private val up: Vector = Vector(0, 1, 0)) {

    val transformation = viewTransformation(from, to, up)
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

    fun withSize(width: Int, height: Int) = copy(width = width, height = height)

    fun withFrom(from: Point) = copy(from = from)

    fun withTo(to: Point) = copy(to = to)

    private fun copy(width: Int = this.width, height: Int = this.height, fov: Double = this.fov,
            from: Point = this.from, to: Point = this.to, up: Vector = this.up) =
        Camera(width, height, fov, from, to, up)


    override fun toString(): String {
        return "Camera(width=$width, height=$height, fov=$fov, transformation=$transformation)"
    }
}

fun pixelSource(scene: Scene) =
    PixelSource(scene.camera.width, scene.camera.height) {
        x, y -> scene.world.colourAt(scene.camera.rayForPixel(x, y)).toAWT()
    }
