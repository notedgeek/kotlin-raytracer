package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.obj.objectFileGroup
import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene = buildScene {
    val scale = 1
    size(3300 / scale, 1340 / scale)

    viewPoint(0.0, 10.0, -15.0)
    lookAt(0.0, 1.0, 0.0)
    pointLight {
        at(-6.0, 10.0, -10.0)
    }
    pointLight {
        at(10.0, 10.0, -10.0)
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


    +plane {
        material {
            pattern {
                checkers(colour(0.8, 0.6, 0.6) * 0.05, colour(0.4, 0.2, 0.2) * 0.05)
                scale(0.5)
            }
            reflective(0.6)
        }
    }

    val dragon = from(objectFileGroup("dragon")) {
        scale(0.8)
        rotateY(-PI / 6)
        material {
            ambient(0.3)
        }
    }

    +from(dragon) {
        material {
            colour(0.5, 0.0, 0.0)
        }
        translateX(-7.0)
    }
    +from(dragon) {
        material {
            colour(0.0, 0.5, 0.0)
        }
        translateX(-2.5)
    }
    +from(dragon) {
        material {
            colour(0.0, 0.2 ,0.2)
            reflective(0.9)
            ambient(0.1)
        }
        translateX(2.5)
    }
    +from(dragon) {
        material {
            colour(0.0, 0.0, 0.8)
        }
        translateX(7.0)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}