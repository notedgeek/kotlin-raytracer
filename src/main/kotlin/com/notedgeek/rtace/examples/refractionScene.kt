package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer

private val scene =
    buildScene (reflectScene){
        sphere {
            scale(0.8)
            translate(1.4,1.5, -2.8)
            material {
                colour(0.1, 0.0, 0.0)
                transparency(0.7)
                refractiveIndex(1.05)
                reflective(0.2)
            }
        }
    }

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}
