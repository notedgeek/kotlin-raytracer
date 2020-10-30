package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.maths.I
import com.notedgeek.rtrace.rotationX
import com.notedgeek.rtrace.rotationZ
import com.notedgeek.rtrace.translation
import kotlin.math.PI

val PEG_IN = I

val PEG_OUT = rotationX(PI)

val EAST_1 = east(1)
val EAST_2 = east(2)
val EAST_13 = east(13)
val EAST_4_6 = east(4, 6)

fun east(y: Int, x: Int = 1) = translation(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH * y) * rotationZ(PI / 2)

val WEST_1 = west(1)
val WEST_3 = west(3)
val WEST_4 = west(4)
val WEST_15 = west(15)

fun west(y: Int, x: Int = 0) = translation(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH * y) * rotationZ(-PI / 2)

val NORTH_1_6 = north(1, 6)
val NORTH_3_6 = north(3, 6)

fun north(x: Int, y: Int) = translation(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH * y) * rotationX(-PI / 2)

fun south(x: Int, y: Int) = translation(BRICK_WIDTH * x, TECH_HOLE_HEIGHT, BRICK_WIDTH * y) * rotationX(PI / 2)

val SOUTH_1 = south(1, 0)
val SOUTH_3 = south(3, 0)

