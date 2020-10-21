package com.notedgeek.rtace.examples

import com.notedgeek.rtace.obj.fromObjectFile
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 3
    size(3300 / scale, 1340 / scale)

    viewPoint(0.0, 7.0, -10.0)
    lookAt(0.0, 2.0, 0.0)
    pointLight {
        at(-3.0, 5.0, -5.0)
    }
    pointLight {
        at(5.0, 5.0, -5.0)
    }

    +plane {
        material {
            colour(1.0, 0.9, 0.9)
            specular(0.0)
        }
    }

    +group(fromObjectFile("dragon")) {
        material {
            colour(0.0, 0.6, 0.0)
        }
        scale(0.8)
        rotateY(-PI / 8)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}