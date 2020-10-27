package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.lego.pegEnd
import com.notedgeek.rtrace.pixelSource
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 2
    size(1920 / scale, 1080 / scale)
    viewPoint(2.0, 5.0, -5.0)
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

    +pegEnd

    +from(pegEnd) {
        translateX(2.0)
        rotateX(-PI / 2)
        translateY(1.0)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}