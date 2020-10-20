package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    size(1920, 1080)
    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0, 0.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
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


    val sphere1 = sphere {}

    val cylinder1 = cappedCylinder {
        translateY(-0.5)
        scale(0.5, 2.5, 0.5)
    }

    +intersect(sphere1, cylinder1) {
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}