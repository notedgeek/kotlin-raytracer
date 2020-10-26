package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.BLACK
import com.notedgeek.rtrace.WHITE
import com.notedgeek.rtrace.obj.objectFileGroup
import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 1
    size(3300 / scale, 1340 / scale)

    viewPoint(0.0, 10.0, -20.0)
    lookAt(0.0, 3.0, 0.0)
    pointLight {
        at(-12.0, 20.0, -20.0)
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
            reflective(0.2)
        }
    }

    val bonnie = from(objectFileGroup("bonnie")) {
        scale(0.075)
        rotateY(PI)
        translateX(squareSize/ 2)
        translateZ(squareSize/ 2)
    }

    for (y in -2..2) {
        +group {
            for (x in 0..7) {
                +from(bonnie) {
                    material {
                        colour(if((x + y) % 2 == 0) WHITE else BLACK)
                        reflective(0.2)
                    }
                    translateX((x - 4) * squareSize)
                    translateZ(-squareSize * y)
                }
            }
        }
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}