package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.buildLegoScene
import com.notedgeek.rtrace.lego.techBar
import com.notedgeek.rtrace.pixelSource
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 1
    size(1920 / scale, 1080 / scale)
    viewPoint(2.0, 5.0, -5.0)
    lookAt(0.0, 0.0, 2.0)

    pointLight {
        at(-5.0, 5.0, -5.0)
    }

    pointLight {
        at(5.0, 5.0, -5.0)
    }


    val piece = from(techBar(4)) {
        material { colour(0.8, 0.8, 0.8) }
    }

    +piece
    +from(piece) {
        rotateY(PI / 4)
        translateX(2.0)
    }
    +from(piece) {
        rotateY(-PI / 3)
        translateX(-2.0)
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}