package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer

private val scene = buildScene {
    size(3300, 1340)

    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0 ,0.0)
    pointLight {
        at(-3.0, 2.8, -3.0)
    }
    pointLight {
        at(3.0, 2.8, -3.0)
    }

    // floor
    +plane {
        material {
            pattern {
                checkers(colour(1.0, 0.9, 0.9), colour(0.5, 0.4, 0.4))
                scale(1.5)
            }
            specular(0.0)
        }
        translateY(-1.0)
    }


    val group1 = group {
        +sphere { translateX(-1.0) }
        +sphere { translateX(1.0) }
        scale(0.25)
    }

    +group(group1) {
        material {
            colour(0.39, 0.26, 0.13)
        }
        translateX(-1.2)
    }

    +group(group1) {
        material {
            colour(0.2, 0.2, 0.2)
            reflective(0.9)
        }
    }

    +group(group1) {
        material {
            colour(1.0, 0.8, 0.8)
        }
        translateX(1.2)
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}