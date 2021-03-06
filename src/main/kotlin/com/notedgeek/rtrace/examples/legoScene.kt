package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.EAST_1
import com.notedgeek.rtrace.lego.EAST_2
import com.notedgeek.rtrace.lego.PEG_IN
import com.notedgeek.rtrace.lego.PEG_OUT
import com.notedgeek.rtrace.lego.WEST_3
import com.notedgeek.rtrace.lego.WEST_4
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.lego.peg
import com.notedgeek.rtrace.lego.techBar
import com.notedgeek.rtrace.pixelSource

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(0.5, 5.0, -6.0)
    lookAt(0.5, 0.0, 0.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
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

    val bar = techBar(5).south()

    +lego {
        +bar
        j(peg, PEG_IN, bar, EAST_1)
        j(peg, PEG_OUT, bar, EAST_2)
        j(peg, PEG_IN, bar, WEST_3)
        j(peg, PEG_OUT, bar, WEST_4)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}