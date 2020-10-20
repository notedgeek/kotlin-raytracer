package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.Point
import com.notedgeek.rtace.WHITE
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
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