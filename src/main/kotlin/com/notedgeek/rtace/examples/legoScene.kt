package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.*

private var scene = buildLegoScene {
    val scale = 10
    size(1920 / scale, 1080 / scale)
    viewPoint(-4.0, 5.0, -5.0)
    lookAt(2.0, 2.0, 0.5)

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

    +lego {
        +brick(2, 4)
    }

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}