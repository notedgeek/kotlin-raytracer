package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.*
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(2.0, 15.0, -15.0)
    lookAt(2.0, 0.0, 2.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
    }


    // floor
    +plane {
        material {
            pattern {
                checkers(colour(0.8, 0.6, 0.6) * 0.5, colour(0.4, 0.2, 0.2) * 0.5)
                scale(1.0)
            }
        }
    }

//    +lego {+from(brick(2, 3)){
//        material {
//            reflective(0.05)
//        }
//        rotateY(PI / 4)
//    } }

    +lego {
        val dim = 10
        for(x in -5 .. dim) {
            for (y in -5 .. dim) {
                for (z in 1 .. dim) {
                    place(plate(1, 1), x - 1, y - 1, z - 1)
                }
            }
        }
        translate(-dim / 5.0, 0.0, -dim / 5.0)
        rotateY(PI / 4)
        translate(dim / 5.0, 0.0, dim / 5.0)
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}