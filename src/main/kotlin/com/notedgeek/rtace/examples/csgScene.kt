package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 2
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


    val hole = cappedCylinder {
        scale(0.15, 2.01, 0.15)
        rotateX(PI / 2)
    }

    +difference {
        material {
            colour(BLACK)
            transparency(0.5)
            refractiveIndex(1.1)
            reflective(0.5)
        }
        +sphere {  }
        val doubleHoleCount = 4
        for(i in 0 until doubleHoleCount) {
            +from(hole) {
                rotateY(i * PI / doubleHoleCount)
            }
        }
        rotateX(PI / 10)
    }


}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}