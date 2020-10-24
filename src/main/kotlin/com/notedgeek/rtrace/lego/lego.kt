@file:Suppress("unused")

package com.notedgeek.rtrace.lego

import com.notedgeek.rtace.maths.Point
import com.notedgeek.rtace.obj.*
import com.notedgeek.rtace.sceneBuilder.*

private const val SCALE = 1.0 / 8

const val BRICK_WIDTH = 8.0 * SCALE
const val BRICK_HEIGHT = 9.6 * SCALE
const val PLATE_HEIGHT = BRICK_HEIGHT / 3
const val STUD_HEIGHT = 1.6 * SCALE
const val STUD_RADIUS = 2.4 * SCALE

val baseCube = buildObject(Cube()) {
    translateY(1.0)
    translateX(1.0)
    translateZ(1.0)
    scale(0.5)
}

val brickCube = buildObject(baseCube) {
    scaleY(BRICK_HEIGHT)
}

val plateCube = buildObject(baseCube) {
    scaleY(PLATE_HEIGHT)
}

val stud = buildObject(Cylinder(min = 0.0, max = STUD_HEIGHT, cappedTop = true)) {
    scale(STUD_RADIUS, 1.0, STUD_RADIUS)
}
fun brickCuboid(width: Int, length: Int) = buildObject(brickCube) { scale(width.toDouble(), 1.0, length.toDouble()) }

fun plateCuboid(width: Int, length: Int) = buildObject(plateCube) { scale(width.toDouble(), 1.0, length.toDouble()) }

fun brick(width: Int, length: Int) = studObject(brickCuboid(width, length), width, BRICK_HEIGHT, length)

fun plate(width: Int, length: Int) = studObject(plateCuboid(width, length), width, PLATE_HEIGHT, length)

private fun studObject(piece: SceneObject, width: Int, height: Double, length: Int) = buildGroup {
    +piece
    for(x in 0 until width) {
        for(z in 0 until length) {
            +buildObject(stud) {
                translateX(BRICK_WIDTH * (0.5 + x))
                translateY(height)
                translateZ(BRICK_WIDTH * (0.5 + z))
            }
        }
    }
}

fun buildLegoScene(block: LegoSceneBuilder.() -> Unit) = LegoSceneBuilder().apply(block).toScene()


class LegoSceneBuilder: SceneBuilder(EMPTY_SCENE) {

    fun lego(block: LegoContext.() -> Unit) = LegoContext().apply(block).toGroup()

}

class LegoContext: GroupBuilder() {

    private var gridX: Int = 0
    private var gridY: Int = 0
    private var gridZ: Int = 0

    var boundingBox: BoundingBox = BoundingBox(
            Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
    )

    fun place(obj: Group, x: Int = this.gridX, y: Int = this.gridY, z: Int = this.gridZ) {
        addObject(obj
                .translateX(x.toDouble())
                .translateY(z * PLATE_HEIGHT)
                .translateZ(y.toDouble()))
    }

    fun lego(block: LegoContext.() -> Unit) = LegoContext().apply(block).toGroup()
}