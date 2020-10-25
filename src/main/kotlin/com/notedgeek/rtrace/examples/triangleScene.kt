package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.BLACK
import com.notedgeek.rtrace.maths.Point
import com.notedgeek.rtrace.WHITE
import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {

    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0, 0.0)
    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    // floor
    +plane {
        material {
            pattern {
                checkers(WHITE, BLACK)
                scale(0.5)
            }
            specular(0.0)
        }
        translateY(-1.0)
    }

    +triangle(
            Point(-1, 0, 0),
            Point(1, 0, 0),
            Point(0, 2, 0)
    ) {
        scale(0.75)
        rotateX(-PI / 3)
        material {
            colour(BLACK)
            reflective(0.9)
        }
    }


}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}