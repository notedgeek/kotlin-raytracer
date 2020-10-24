@file:Suppress("DuplicatedCode")

package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val reflectScene =
    buildScene (backdrop1){
        // left sphere
        +sphere {
            material {
                pattern {
                    stripes(colour(0.5, 1.0, 0.1), colour(0.8, 0.1, 0.8))
                    scale(0.3, 1.0, 1.0)
                    rotateZ(PI / 2.0)
                }
                diffuse(0.7)
                specular(0.3)
            }
            scale(0.4)
            translate(0.0, 0.4, -1.8)
        }
        // middle sphere
        +sphere {
            material {
                colour(0.1, 0.1, 0.1)
                diffuse(0.7)
                specular(1.0)
                shininess(500.0)
                reflective(1.0)
            }
            translate(-0.5, 1.0, 0.5)
        }
        // right sphere
        +sphere {
            material {
                colour(0.5, 1.0, 0.1)
                diffuse(0.7)
                specular(0.3)
            }
            scale(0.5)
            translate(1.5, 0.5, -0.5)
        }

        val littleSphere = sphere {
            material {
                colour(0.8, 0.1, 0.8)
                diffuse(0.7)
                specular(0.3)
            }
            scale(0.1)
            translate(0.0, 0.1, -1.8)
        }

        val sphereCount = 24
        val angleInc = PI * 2.0 / sphereCount
        for (i in 0 until sphereCount) {
            +from(littleSphere) {
                val angle = i * angleInc
                translate(cos(angle), 0.4, sin(angle))
                if (i % 2 == 0) {
                    material {
                        colour(BLACK)
                        reflective(1.0)
                    }
                }
            }
        }
    }

fun main() {
    PixelSourceRenderer(pixelSource(reflectScene))
}
