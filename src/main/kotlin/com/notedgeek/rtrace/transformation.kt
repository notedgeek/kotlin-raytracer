package com.notedgeek.rtrace

import com.notedgeek.rtrace.maths.Matrix
import kotlin.math.cos
import kotlin.math.sin

fun translation(x: Double, y: Double, z: Double) = Matrix(
    1.0, 0.0, 0.0, x,
    0.0, 1.0, 0.0, y,
    0.0, 0.0, 1.0, z,
    0.0, 0.0, 0.0, 1.0
)

fun scaling(x: Double, y: Double, z: Double) = Matrix(
    x, 0.0, 0.0, 0.0,
    0.0, y, 0.0, 0.0,
    0.0, 0.0, z, 0.0,
    0.0, 0.0, 0.0, 1.0
)

fun rotationX(r: Double) = Matrix(
    1.0, 0.0, 0.0, 0.0,
    0.0, cos(r), -sin(r), 0.0,
    0.0, sin(r), cos(r), 0.0,
    0.0, 0.0, 0.0, 1.0
)

fun rotationY(r: Double) = Matrix(
    cos(r), 0.0, sin(r), 0.0,
    0.0, 1.0, 0.0, 0.0,
    -sin(r), 0.0, cos(r), 0.0,
    0.0, 0.0, 0.0, 1.0
)

fun rotationZ(r: Double) = Matrix(
    cos(r), -sin(r), 0.0, 0.0,
    sin(r), cos(r), 0.0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
)

fun shearing(xy: Double, xz: Double, yx: Double, yz: Double, zx: Double, zy: Double) =
    Matrix(
        1.0, xy, xz, 0.0,
        yx, 1.0, yz, 0.0,
        zx, zy, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
