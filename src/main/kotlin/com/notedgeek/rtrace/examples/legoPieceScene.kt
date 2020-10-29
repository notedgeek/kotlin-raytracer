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
import com.notedgeek.rtrace.rotationY
import com.notedgeek.rtrace.sceneBuilder.buildGroup
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(2.0, 15.0, -15.0)
    lookAt(0.0, 0.0, 2.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
    }

    val bar = from(techBar(5)) {
        rotateY(PI / 2)
        translate(2.0, 0.0, 3.0)
    }

    val group = buildGroup {
        +bar
        +from(peg) {
            join(PEG_IN, bar, EAST_1)
        }
        +from(peg) {
            join(PEG_OUT, bar, EAST_2)
        }
        +from(peg) {
            join(PEG_IN, bar, WEST_3)
        }
        +from(peg) {
            join(PEG_OUT, bar, WEST_4)
        }
    }

    +from(group) {
        transformAtOrigin(rotationY(PI / 2))
    }



}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}