package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.pixelSource

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(-20.0, 20.0, -20.0)
    lookAt(0.5, 0.0, 2.0)

    pointLight {
        at(-15.0, 15.0, -15.0)
    }

    pointLight {
        at(15.0, 15.0, 15.0)
    }

    //floor
    +plane {
        material {
            pattern {
                checkers(colour(0.8, 0.6, 0.6) * 0.5, colour(0.4, 0.2, 0.2) * 0.5)
                scale(1.0)
            }
        }
    }

    //sky
    +plane {
        material {
            colour(1.0, 1.0, 1.0)

        }
        translateY(50.0)
    }

    +section01
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}