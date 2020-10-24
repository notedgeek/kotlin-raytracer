package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    var scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(0.0, 0.0, -5.0)
    pointLight {
        at(-5.0, 5.0, -5.0)
    }
    pointLight {
        at(5.0, 5.0, -5.0)
    }
    +group {
        val rMin = 0.25
        +torus(1.0, rMin) {
            material {
                colour(0.2, 0.0, 0.0)
                transparency(0.9)
                refractiveIndex(1.1)
                reflective(0.5)
            }
            rotateX(-PI / 2)
            translateX(-1.0 - rMin * 1.2)
        }
        +torus(1.0, rMin) {
            material {
                colour(0.2, 0.0, 0.0)
                transparency(0.9)
                refractiveIndex(1.1)
                reflective(0.5)
            }
            scale(1.2, 1.0, 1.0)
        }
        +torus(1.0, rMin) {
            material {
                colour(0.2, 0.0, 0.0)
                transparency(0.9)
                refractiveIndex(1.1)
                reflective(0.5)
            }
            rotateX(-PI / 2)
            translateX(1.0 + rMin * 1.2)
        }
        rotateX(PI / 4)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}