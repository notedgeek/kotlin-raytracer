package com.notedgeek.rtace.examples

import com.notedgeek.rtace.Colour
import com.notedgeek.rtace.obj.objectFileGroup
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)

    viewPoint(0.0, 10.0, -16.0)
    lookAt(0.0, 4.5, 0.0)
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
            colour(Colour(1.0, 0.9, 0.9) * 0.5 )
            specular(0.0)
        }
        rotateX(-PI / 2)
        translateZ(squareSize * 3)
    }


    +plane {
        material {
        }
    }

    val armadillo = from(objectFileGroup("armadillo")) {
        translateY(54.2415)
        scale(0.06)
        rotateY(PI / 6)
    }

    +from(armadillo) {
        material {
            colour(0.0, 0.5, 0.0)
        }
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}