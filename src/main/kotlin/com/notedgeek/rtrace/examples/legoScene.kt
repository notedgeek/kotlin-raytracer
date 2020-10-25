package com.notedgeek.rtrace.examples

import com.notedgeek.rtrace.pixelSource
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import com.notedgeek.rtrace.lego.*
import kotlin.math.PI

private var scene = buildLegoScene {
    val scale = 2
    size(1920 / scale, 1080 / scale)
    viewPoint(0.0, 20.0, -15.0)
    lookAt(0.0, 2.0, 0.0)

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

    val l = lego {
        place(plate(2, 1))
        place(plate(1,1), y = 1)
        rotateY(PI / 4)
    }

    val compound1 = lego {
        +from(l) {
            scale(2.0)
        }
        +from(l) {
            translateY(PLATE_HEIGHT * 4)
            rotateY(PI / 8)
        }
    }

    val compound2 = group {
        +compound1
        +from(compound1) {
            rotateX(-PI / 2)
            translateZ(8.0)
            translateY(4.0)
        }
    }

    +group {
        +compound2
        +from(compound2) {
            translateX(-5.0)
        }
    }
}

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}