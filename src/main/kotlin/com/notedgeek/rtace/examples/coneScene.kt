@file:Suppress("DuplicatedCode")

package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0 ,0.0)
    pointLight {
        at(-3.0, 2.8, -3.0)
    }
    pointLight {
        at(0.0, -2.8, 0.0)
    }

    // floor
    +plane {
        material {
            pattern {
                checkers(colour(1.0, 0.9, 0.9), colour(0.5, 0.4, 0.4))
                scale(0.25)
            }
            specular(0.0)
        }
        translateY(-3.0)
    }

    +cone {
        material {
            reflective(0.8)
        }
        rotateX(PI / 6.0)
        translateX(-1.2)
    }
    +cappedCone {
        material {
            reflective(0.8)
        }
        rotateX(PI / 6.0)
        translateX(1.2)
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}