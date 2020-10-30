package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.lego.steeringWheelBase
import com.notedgeek.rtrace.pixelSource

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(6.0, 8.0, -8.0)
    lookAt(6.0, 0.0, 2.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
    }

    +steeringWheelBase()

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}