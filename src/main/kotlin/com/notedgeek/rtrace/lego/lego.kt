@file:Suppress("unused")

package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.maths.Matrix
import com.notedgeek.rtrace.obj.Cube
import com.notedgeek.rtrace.obj.Group
import com.notedgeek.rtrace.obj.SceneObject
import com.notedgeek.rtrace.rotationY
import com.notedgeek.rtrace.sceneBuilder.EMPTY_SCENE
import com.notedgeek.rtrace.sceneBuilder.GroupBuilder
import com.notedgeek.rtrace.sceneBuilder.SceneBuilder
import com.notedgeek.rtrace.sceneBuilder.buildGroup
import com.notedgeek.rtrace.sceneBuilder.buildObject
import com.notedgeek.rtrace.translation
import kotlin.math.PI

internal const val SCALE = 1.0 / 8

const val BRICK_WIDTH = 8.0 * SCALE
const val BRICK_HEIGHT = 9.6 * SCALE
const val PLATE_HEIGHT = BRICK_HEIGHT / 3
const val STUD_HEIGHT = 1.6 * SCALE
const val STUD_RADIUS = 2.4 * SCALE
const val TECH_HOLE_RADIUS = 2.4 * SCALE
const val TECH_HOLE_HEIGHT = BRICK_HEIGHT / 1.6
const val TECH_SHOULDER_RADIUS = 3.1 * SCALE
const val TECH_SHOULDER_DEPTH = 0.8 * SCALE
const val TECH_STUD_HOLE_RADIUS = 1.6 * SCALE
const val TECH_STUD_HOLE_DEPTH = STUD_HEIGHT * 0.95
const val TECH_PEG_INNER_HOLE_RADIUS = 2.2 * SCALE

class CuboidBased(private val group: Group, private val width: Int, private val length: Int) : Group(group.children, group.material, group.transform, group.parent) {

    fun north() = withTransform(I)
    fun east() = withTransform(translation(0.0, 0.0, width.toDouble()) * rotationY(PI / 2))
    fun south() = withTransform(translation(width.toDouble(), 0.0, length.toDouble()) * rotationY(PI))
    fun west() = withTransform(translation(length.toDouble(), 0.0, 0.0) * rotationY(3 * PI / 2))

    override fun withMaterial(material: Material): CuboidBased {
        return CuboidBased(group.withMaterial(material), width, length)
    }
}


fun brick(width: Int, length: Int) =
        CuboidBased(studObject(pieceCuboid(width, length, BRICK_HEIGHT), width, BRICK_HEIGHT, length, stud), width, length)

fun plate(width: Int, length: Int) = CuboidBased(studObject(pieceCuboid(width, length, PLATE_HEIGHT), width, PLATE_HEIGHT, length), width, length)

fun smoothPlate(width: Int, length: Int) = CuboidBased(pieceCuboid(width, length, PLATE_HEIGHT), width, length)

fun steeringWheelBase() = buildGroup {
    +union {
        +smoothPlate(2, 1)
        +from(techStud) {
            translate(BRICK_WIDTH, PLATE_HEIGHT, BRICK_WIDTH / 2)
        }
    }
}

fun techBar(length: Int) =
        CuboidBased(techHoleObject(
                studObject(pieceCuboid(1, length, BRICK_HEIGHT), 1, BRICK_HEIGHT, length, techStud), length
        ), 1, length)

val baseCube = buildObject(Cube()) {
    translateY(1.0)
    translateX(1.0)
    translateZ(1.0)
    scale(0.5)
}

fun pieceCuboid(width: Int, length: Int, height: Double) = buildGroup {
    val overlap = 0.01
    val bevelSize = 0.02
    val xBevel = buildObject(baseCube) {
        scale(width + overlap, bevelSize, bevelSize)
        translate(-overlap / 2, -bevelSize / 2, -bevelSize / 2)
        rotateX(PI / 4)
    }
    val yBevel = buildObject(baseCube) {
        scale(bevelSize, height + overlap, bevelSize)
        translate(-bevelSize / 2, -overlap / 2, -bevelSize / 2)
        rotateY(PI / 4)
    }
    val zBevel = buildObject(baseCube) {
        scale(bevelSize, bevelSize, length + overlap)
        translate(-bevelSize / 2, -bevelSize / 2, -overlap / 2)
        rotateZ(PI / 4)
    }
    +difference {
        +from(baseCube) {
            scale(width.toDouble(), height, length.toDouble())
        }
        +xBevel
        +from(xBevel) {
            translateY(height)
        }
        +from(xBevel) {
            translateZ(length.toDouble())
        }
        +from(xBevel) {
            translate(0.0, height, length.toDouble())
        }
        +yBevel
        +from(yBevel) {
            translateZ(length.toDouble())
        }
        +from(yBevel) {
            translateX(width.toDouble())
        }
        +from(yBevel) {
            translate(width.toDouble(), 0.0, length.toDouble())
        }
        +zBevel
        +from(zBevel) {
            translateY(height)
        }
        +from(zBevel) {
            translateX(width.toDouble())
        }
        +from(zBevel) {
            translate(width.toDouble(), height, 0.0)
        }
    }
}

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

val techStud = buildGroup {
    +difference {
        +stud
        +cappedCylinder {
            scale(TECH_STUD_HOLE_RADIUS, TECH_STUD_HOLE_DEPTH * 1.001, TECH_STUD_HOLE_RADIUS)
            translateY(STUD_HEIGHT - TECH_STUD_HOLE_DEPTH)
        }
        +difference {
            +cappedCone {}
            +cube {
                scale(1.001, 1.0, 1.001)
                translateY(1.2)
            }
            rotateX(PI)
            scale(TECH_STUD_HOLE_RADIUS * 1.2)
            translateY(STUD_HEIGHT * 1.1)
        }
    }
}

val techHole = buildGroup {
    +union {
        +cappedCylinder {
            scale(TECH_HOLE_RADIUS, BRICK_WIDTH, TECH_HOLE_RADIUS)
            rotateZ(PI / 2)
        }
        +cappedCylinder {
            scale(TECH_SHOULDER_RADIUS, TECH_SHOULDER_DEPTH + 0.01, TECH_SHOULDER_RADIUS)
            rotateZ(PI / 2)
            translateX(-(BRICK_WIDTH - TECH_SHOULDER_DEPTH + 0.01))
        }
        +cappedCylinder {
            scale(TECH_SHOULDER_RADIUS, TECH_SHOULDER_DEPTH + 0.01, TECH_SHOULDER_RADIUS)
            rotateZ(PI / 2)
            translateX(0.01)
        }
    }
}

val flangedTechHole = buildGroup {
    +union {
        +techHole
        +cube {
            translate(1.0, 1.0, 1.0)
            scale(0.5)
            scale(TECH_SHOULDER_DEPTH + 0.01, TECH_HOLE_HEIGHT + 0.01, TECH_SHOULDER_RADIUS * 2)
            translate(-TECH_SHOULDER_DEPTH, -TECH_HOLE_HEIGHT - 0.01, -TECH_SHOULDER_RADIUS)
        }
    }
}

private fun studObject(piece: SceneObject, width: Int, height: Double, length: Int, studType: SceneObject = stud) = buildGroup {
    +union {
        +piece
        for (x in 0 until width) {
            for (z in 0 until length) {
                +buildObject(studType) {
                    translateX(BRICK_WIDTH * (0.5 + x))
                    translateY(height)
                    translateZ(BRICK_WIDTH * (0.5 + z))
                }
            }
        }
    }
}

private fun techHoleObject(piece: SceneObject, length: Int) = buildGroup {
    +difference {
        +piece
        for (z in 1 until length) {
            +buildObject(techHole) {
                translate(BRICK_WIDTH, TECH_HOLE_HEIGHT, BRICK_WIDTH * z)
            }
        }
    }
}

fun techSquareRing(length: Int): Group {
    val width = length - 2
    return buildGroup {
        +difference {
            +union {
                +pieceCuboid(width, length, BRICK_HEIGHT)
                for (i in 1..width) {
                    val x = BRICK_WIDTH * (i - 0.5)
                    +from(techStud) {
                        translate(x, BRICK_HEIGHT, BRICK_WIDTH / 2)
                    }
                    +from(techStud) {
                        translate(x, BRICK_HEIGHT, BRICK_WIDTH / 2 + (length - 1) * BRICK_WIDTH)
                    }
                }
                for (i in 2 until length) {
                    val y = (i - 0.5) * BRICK_WIDTH
                    +from(techStud) {
                        translate(BRICK_WIDTH / 2, BRICK_HEIGHT, y)
                    }
                    +from(techStud) {
                        translate(BRICK_WIDTH * (width - 0.5), BRICK_HEIGHT, y)
                    }
                }
            }
            +from(baseCube) {
                scale(width - 2.0, BRICK_HEIGHT + 0.01, length - 2.0)
                translate(1.0, -0.005, 1.0)
            }
            val xs = if (length == 8) intArrayOf(2, 3, 4) else intArrayOf(1, 2, 3)
            for (x in xs) {
                +from(flangedTechHole) {
                    rotateY(-PI / 2)
                    translate(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH)
                }
                +from(flangedTechHole) {
                    rotateY(PI / 2)
                    translate(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH * length - 1)
                }
            }
            val ys = if (length == 8) intArrayOf(1, 2, 3, 4, 5, 6, 7) else intArrayOf(2, 3, 4)
            for (y in ys) {
                +from(flangedTechHole) {
                    translate(BRICK_WIDTH, TECH_HOLE_HEIGHT, BRICK_WIDTH * y)
                }
                +from(flangedTechHole) {
                    rotateY(PI)
                    translate(BRICK_WIDTH * (width - 1), TECH_HOLE_HEIGHT, BRICK_WIDTH * y)
                }
            }
        }
    }
}

fun buildLegoScene(block: LegoSceneBuilder.() -> Unit) = LegoSceneBuilder().apply(block).toScene()


class LegoSceneBuilder : SceneBuilder(EMPTY_SCENE) {

    fun lego(block: LegoContext.() -> Unit) = LegoContext().apply(block).toGroup()

}

fun lego(block: LegoContext.() -> Unit) = LegoContext().apply(block).toGroup()

class LegoContext(private val map: MutableMap<String, SceneObject> = HashMap()) : GroupBuilder() {

    private var gridX: Int = 0
    private var gridY: Int = 0
    private var gridZ: Int = 0

    fun p(x: Int = 0, y: Int = 0, z: Int = 0, block: LegoContext.() -> Unit) = p(LegoContext(map).apply(block).toGroup(), x, y, z)

    fun p(obj: SceneObject, x: Int = this.gridX, y: Int = this.gridY, z: Int = this.gridZ): SceneObject {
        gridX = x
        gridY = y
        gridZ = z
        val result = obj
                .translateX(x.toDouble())
                .translateY(z * PLATE_HEIGHT)
                .translateZ(y.toDouble())
        addObject(result)
        return result
    }

    fun resetXYZ() = setXYZ(0, 0, 0)

    fun setXYZ(x: Int, y: Int, z: Int) {
        gridX = x
        gridY = y
        gridZ = z
    }

    fun setX(n: Int) {
        gridX = n
    }

    fun setY(n: Int) {
        gridY = n
    }

    fun setZ(n: Int) {
        gridZ = n
    }

    fun s(string: String, obj: SceneObject) {
        map[string] = obj
    }

    fun g(string: String): SceneObject = map[string] ?: Group()

    fun lego(block: LegoContext.() -> Unit) = LegoContext(map).apply(block).toGroup()

    fun j(fromObject: SceneObject, fromVector: Matrix, toObject: SceneObject, toVector: Matrix, rotation: Double = 0.0) =
            j(fromObject, fromObject, fromVector, toObject, toVector, rotation)

    fun j(target: SceneObject, fromObject: SceneObject, fromVector: Matrix, toObject: SceneObject, toVector: Matrix, rotation: Double = 0.0) =
            +target.transform(toObject.transform * toVector * rotationY(rotation) * -fromVector * -fromObject.transform)

}