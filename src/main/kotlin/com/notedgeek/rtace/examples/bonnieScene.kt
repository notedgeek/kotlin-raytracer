package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.WHITE
import com.notedgeek.rtace.obj.fromObjectFile
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 10
    size(3300 / scale, 1340 / scale)

    viewPoint(0.0, 10.0, -20.0)
    lookAt(0.0, 3.0, 0.0)
    pointLight {
        at(-6.0, 10.0, -10.0)
    }
    pointLight {
        at(10.0, 10.0, -10.0)
    }

    val squareSize = 6.0

    // back wall
    +plane {
        material {
            colour(1.0, 0.9, 0.9)
            specular(0.0)
        }
        rotateX(-PI / 2)
        translateZ(squareSize * 3)
    }


    +plane {
        material {
            pattern {
                checkers(BLACK, WHITE)
                scale(squareSize)
            }
            reflective(0.6)
        }
    }

    val bonnie = group(fromObjectFile("bonnie")) {
        material {
            colour(0.4, 0.0, 0.0)
            reflective(0.2)
        }
        scale(0.075)
        rotateY(PI)
        translateX(squareSize/ 2)
        translateZ(squareSize/ 2)
    }

    for(x in 0..7) {
        for(y in -2..2) {
            +from(bonnie) {
                translateX((x - 4) * squareSize)
                translateZ(-squareSize * y)
            }
        }
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}