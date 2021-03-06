package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.WHITE
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.pixelSource
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 1.3
    size((1920 * scale).toInt(), (1080 * scale).toInt())
    val vpScale = 8
    viewPoint(-3.0 * vpScale, 4.0 * vpScale, -6.0 * vpScale)
    lookAt(0.5, -10.0, 2.0)

    pointLight {
        at(-35.0, 35.0, -40.0)
    }

    pointLight {
        at(35.0, 35.0, -40.0)
    }

    //floor
    +plane {
        material {
            pattern {
                checkers(WHITE * 1.0, WHITE * 0.8)
                scale(1.0)
            }
        }
        translateY(-0.25)
    }

    //sky
    +plane {
        material {
            colour(1.0, 1.0, 1.0)

        }
        translateY(100.0)
    }

    +from(section01) {
        rotateY(PI / 2)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}