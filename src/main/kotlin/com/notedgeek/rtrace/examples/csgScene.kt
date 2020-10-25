package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.BLACK
import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0, 0.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
    }

    // floor
    +plane {
        material {
            pattern {
                checkers(colour(0.8, 0.6, 0.6) * 0.5, colour(0.4, 0.2, 0.2) * 0.5)
                scale(0.5)
            }
            reflective(0.6)
        }
        translateY(-1.1)
    }

    // back wall
    +plane {
        material {
            colour(1.0, 0.9, 0.9)
            specular(0.0)
        }
        rotateX(-PI / 2)
        translateZ(4.0)
    }

    val holeSize = 0.1
    val doubleHoleCount = 6
    val hole = cappedCylinder {
        scale(holeSize, 2.01, holeSize)
        rotateX(PI / 2)
    }
    val holeyBall = group {
        +difference {
            material {
                colour(BLACK)
                transparency(0.5)
                refractiveIndex(1.1)
                reflective(0.5)
            }
            +sphere { }
            for (i in 0 until doubleHoleCount) {
                +from(hole) {
                    rotateY(i * PI / doubleHoleCount)
                }
            }
            rotateX(PI / 20)
            scale(0.5)
        }
    }

    val twoBalls = group {
        val offset = 0.8
        +from(holeyBall) {
            translateX(-offset)
        }
        +from(holeyBall) {
            translateX(+offset)
        }
        rotateX(PI / 4)
    }

    +group {
        val offset = 0.6
        +from(twoBalls) {
            translateY(-offset)
        }
        +from(twoBalls) {
            translateY(+offset)
        }
        translateY(0.4)
        rotateY(PI / 6)
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}