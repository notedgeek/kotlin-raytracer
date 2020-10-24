@file:Suppress("DuplicatedCode")

package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer

private val scene = buildScene {
    size(3300, 1340)

    viewPoint(0.0, 0.0, -5.0)
    lookAt(0.0, 0.0 ,0.0)
    pointLight {
        at(-3.0, 2.8, -3.0)
    }
    pointLight {
        at(3.0, 2.8, -3.0)
    }

    val group1 = group {
        +sphere {
            scale(0.25)
            translateX(-0.30)
        }
        +sphere {
            scale(0.25)
            translateX(-0.9)
        }
        +sphere {
            scale(0.25)
            translateX(0.30)
        }
        +sphere {
            scale(0.25)
            translateX(0.9)
        }
    }.split(2)

    +from(group1)
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}