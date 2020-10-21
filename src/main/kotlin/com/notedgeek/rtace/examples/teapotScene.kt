package com.notedgeek.rtace.examples

import com.notedgeek.rtace.obj.fromObjectFile
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 1
    size(3300 / scale, 1340 / scale)

    viewPoint(0.0,  3.0, -6.0)
    lookAt(0.0, 0.0, 0.0)
    pointLight {
        at(-3.0, 5.0, -5.0)
    }
    pointLight {
        at(5.0, 5.0, -5.0)
    }

    +plane {
        material {
            pattern {
                checkers(colour(0.8, 0.6, 0.6), colour(0.4, 0.2, 0.2))
                scale(0.5)
            }
            specular(0.0)
        }
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

    val b = true
    val s = true

    +group(fromObjectFile("teapot-low", false, bound = b, split = s)) {
        material {
            colour(0.7, 0.0, 0.0)
        }
        scale(1.0 / 15)
        rotateX(-PI / 2)
        translateX(-1.5)
        translateZ( 1.5)
    }
    +group(fromObjectFile("teapot-low", true, bound = b, split = s)) {
        material {
            colour(0.7, 0.0, 0.0)
        }
        scale(1.0 / 15)
        rotateX(-PI / 2)
        translateX(1.5)
        translateZ( 1.5)
    }
    +group(fromObjectFile("teapot", true, bound = b, split = s)) {
        material {
            colour(0.2, 0.0, 0.0)
            reflective(0.8)
        }
        scale(1.0 / 15)
        rotateX(-PI / 2)
    }
    +group(fromObjectFile("teapot", false, bound = b, split = s)) {
        material {
            colour(0.7, 0.0, 0.0)
        }
        scale(1.0 / 15)
        rotateX(-PI / 2)
        translateX(-1.5)
        translateZ( -1.5)
    }
    +group(fromObjectFile("teapot", true, bound = b, split = s)) {
        material {
            colour(0.7, 0.0, 0.0)
        }
        scale(1.0 / 15)
        rotateX(-PI / 2)
        translateX(1.5)
        translateZ( -1.5)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}