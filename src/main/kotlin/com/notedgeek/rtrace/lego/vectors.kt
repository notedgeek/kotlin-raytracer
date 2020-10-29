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
val EAST_3 = east(3)
val EAST_4 = east(4)
val EAST_5 = east(5)
val EAST_6 = east(6)

fun east(y: Int) = translation(0.0, BRICK_HEIGHT / 2, BRICK_WIDTH * y) * rotationZ(-PI / 2)

val WEST_1 = west(1)
val WEST_2 = west(2)
val WEST_3 = west(3)
val WEST_4 = west(4)
val WEST_5 = west(5)
val WEST_6 = west(6)

fun west(y: Int) = translation(BRICK_WIDTH, BRICK_HEIGHT / 2, BRICK_WIDTH * y) * rotationZ(PI / 2)
