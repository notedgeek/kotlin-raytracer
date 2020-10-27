@file:Suppress("unused")

package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.obj.*
import com.notedgeek.rtrace.sceneBuilder.*
import kotlin.math.PI

private const val SCALE = 1.0 / 8

const val BRICK_WIDTH = 8.0 * SCALE
const val BRICK_HEIGHT = 9.6 * SCALE
const val PLATE_HEIGHT = BRICK_HEIGHT / 3
const val STUD_HEIGHT = 1.6 * SCALE
const val STUD_RADIUS = 2.4 * SCALE
const val TECH_HOLE_RADIUS = 2.4 * SCALE
const val TECH_SHOULDER_RADIUS = 3.1 * SCALE
const val TECH_SHOULDER_DEPTH = 0.8 * SCALE
const val TECH_INNER_STUD_HOLE_RADIUS = 1.6 * SCALE
const val TECH_PEG_INNER_HOLE_RADIUS = 2.2 * SCALE

val baseCube = buildObject(Cube()) {
    translateY(1.0)
    translateX(1.0)
    translateZ(1.0)
    scale(0.5)
}

fun pieceCube(height: Double) = buildGroup {
    val overlap = 0.01
    val bevelSize = 0.02
    val xBevel = buildObject(baseCube) {
        scale(1 + overlap, bevelSize, bevelSize)
        translate(-overlap / 2, -bevelSize / 2, -bevelSize / 2)
        rotateX(PI / 4)
    }
    val yBevel = buildObject(baseCube) {
        scale(bevelSize, height + overlap, bevelSize)
        translate(-bevelSize / 2, -overlap / 2, -bevelSize / 2)
        rotateY(PI / 4)
    }
    val zBevel = buildObject(baseCube) {
        scale(bevelSize, bevelSize, 1 + overlap)
        translate(-bevelSize / 2, -bevelSize / 2, -overlap / 2)
        rotateZ(PI / 4)
    }
    +difference {
        +from(baseCube) {
            scaleY(height)
        }
        +xBevel
        +from(xBevel) {
            translateY(height)
        }
        +from(xBevel) {
            translateZ(1.0)
        }
        +from(xBevel) {
            translate(0.0, height, 1.0)
        }
        +yBevel
        +from(yBevel) {
            translateZ(1.0)
        }
        +from(yBevel) {
            translateX(1.0)
        }
        +from(yBevel) {
            translate(1.0, 0.0, 1.0)
        }
        +zBevel
        +from(zBevel) {
            translateY(height)
        }
        +from(zBevel) {
            translateX(1.0)
        }
        +from(zBevel) {
            translate(1.0, height, 0.0)
        }
    }
}

val brickSquare = pieceCube(BRICK_HEIGHT)

val plateSquare = pieceCube(PLATE_HEIGHT)

val stud = buildGroup {
    +difference {
        +cappedCylinder {
            scale(STUD_RADIUS, STUD_HEIGHT, STUD_RADIUS)
        }
        +difference {
            +cappedCylinder {
                scale(STUD_RADIUS * 1.1, STUD_HEIGHT, STUD_RADIUS * 1.1)
            }
            +cappedCone {
                scale(STUD_RADIUS, STUD_HEIGHT, STUD_RADIUS)
            }
            translateY(STUD_HEIGHT * 0.9)
        }
    }

}

fun brickCuboid(width: Int, length: Int) = buildObject(brickSquare) { scale(width.toDouble(), 1.0, length.toDouble()) }

fun plateCuboid(width: Int, length: Int) = buildObject(plateSquare) { scale(width.toDouble(), 1.0, length.toDouble()) }

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

val pegEnd = buildCsg(CSGOperation.UNION) {
    val ridgeRad = 0.1 * SCALE
    val ridgeHeight = 0.1 * SCALE

    +cappedCylinder {
        scale(TECH_HOLE_RADIUS + ridgeRad, ridgeHeight, TECH_HOLE_RADIUS + ridgeRad)
        translateY(1 - ridgeHeight)
    }
    +difference {
        +cappedCylinder {
            scale(TECH_SHOULDER_RADIUS, 1.0, TECH_SHOULDER_RADIUS)
        }
        +difference {
            +cappedCylinder {
                scale(TECH_SHOULDER_RADIUS * 1.01, 1.0, TECH_SHOULDER_RADIUS * 1.01)
            }
            +cappedCylinder {
                scale(TECH_HOLE_RADIUS, 1.01, TECH_HOLE_RADIUS)
                translateY(-0.005)
            }
            translateY(TECH_SHOULDER_DEPTH)
        }
        +cappedCylinder {
            scale(TECH_PEG_INNER_HOLE_RADIUS, 1.01, TECH_PEG_INNER_HOLE_RADIUS)
            translateY(-0.005)
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