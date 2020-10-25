@file:Suppress("DuplicatedCode")

package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.sceneBuilder.buildScene
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

    val group2 = group {
        +from(group1) {
            material {
                colour(0.39, 0.26, 0.13)
            }
            translateX(-1.2)
        }

        +from(group1) {
            material {
                colour(0.2, 0.2, 0.2)
                reflective(0.9)
            }
        }

        +from(group1) {
            material {
                colour(1.0, 0.8, 0.8)
            }
            translateX(1.2)
        }
    }

    val group3 = group {
        for(i in 0 until 3) {
            +from(group2) {
                translateY(0.5 * i)
            }
        }
    }

    +group {
        for(i in 0 until 3) {
            +from(group3) {
                translateZ(0.5 * i)
            }
        }
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}