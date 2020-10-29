package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.lego.techSquareRing
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

    +from(techSquareRing(6)) {
        translateX(9.0)
    }

    +techSquareRing(8)

}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}