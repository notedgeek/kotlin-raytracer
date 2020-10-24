package com.notedgeek.rtace.examples

import com.notedgeek.rtace.*
import com.notedgeek.rtace.maths.Point
import com.notedgeek.rtace.obj.Plane
import com.notedgeek.rtace.obj.Sphere
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private const val width = 2000
private const val height = 1000

private val floor = Plane()
    .withMaterial(Material(colour = Colour(1.0, 0.9, 0.9), specular = 0.0))

private val backWall = Plane()
    .rotateX(-PI / 2)
    .translateZ(3.0)
    .withMaterial(floor.material)

private val leftWall = Plane()
    .rotateZ(-PI / 2)
    .translateX(-3.0)
    .withMaterial(floor.material)

private val middle = Sphere()
    .translate(-0.5, 1.0, 0.5)
    .colour(0.1, 1.0, 0.5)
    .diffuse(0.7)
    .specular(0.3)

private val right = Sphere()
    .scale(0.5, 0.5, 0.5)
    .translate(1.5, 0.5, -0.5)
    .colour(0.5, 1.0, 0.1)
    .diffuse(0.7)
    .specular(0.3)

private val left = Sphere()
    .scale(0.33, 0.33, 0.33)
    .translate(-1.5, 0.33, -0.75)
    .colour(1.0, 0.8, 0.1)
    .diffuse(0.7)
    .specular(0.3)


private val lights = listOf(
    PointLight(Point(-1, 10, -10), WHITE),
    PointLight(Point(5, 5, -5), WHITE)
)

private val world = World(lights, listOf(
    floor, backWall, leftWall, middle, right, left
))

private val camera = Camera(width, height, PI / 3, Point(0.0, 1.5, -5.0), Point(0.0, 1.0, 0.0))

fun main() {
    PixelSourceRenderer(pixelSource(Scene(world, camera)))
}