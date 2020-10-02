package com.notedgeek.rtace.examples

import com.notedgeek.rtace.*
import com.notedgeek.rtace.objects.sphere
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private const val width = 2000
private const val height = 1000

private val floor = sphere()
    .scale(10.0, 0.01, 10.0)
    .material(material(colour = colour(1.0, 0.9, 0.9), specular = 0.0))

private val leftWall = sphere()
    .scale(10.0, 0.01, 10.0)
    .rotateX(PI / 2)
    .rotateY(-PI / 4)
    .translateZ(5.0)
    .material(floor.material)

private val rightWall = sphere()
    .scale(10.0, 0.01, 10.0)
    .rotateX(PI / 2)
    .rotateY(PI / 4)
    .translateZ(5.0)
    .material(floor.material)

private val middle = sphere()
    .translate(-0.5, 1.0, 0.5)
    .colour(0.1, 1.0, 0.5)
    .diffuse(0.7)
    .specular(0.3)

private val right = sphere()
    .scale(0.5, 0.5, 0.5)
    .translate(1.5, 0.5, -0.5)
    .colour(0.5, 1.0, 0.1)
    .diffuse(0.7)
    .specular(0.3)

private val left = sphere()
    .scale(0.33, 0.33, 0.33)
    .translate(-1.5, 0.33, -0.75)
    .colour(1.0, 0.8, 0.1)
    .diffuse(0.7)
    .specular(0.3)


private val light = pointLight(point(-10, 10, -10), WHITE)

private val world = world(light, listOf(
    floor, leftWall, rightWall, middle, right, left
))

private val camera = camera(width, height, PI / 3, viewTransformation(
    point(0.0, 1.5, -5.0), point(0.0, 1.0, 0.0)))

fun main() {
    PixelSourceRenderer(pixelSource(world, camera, width, height))
}