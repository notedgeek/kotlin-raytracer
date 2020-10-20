package com.notedgeek.rtace.examples

import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private fun scene() =
    buildScene {
        size(1920, 1080)
        viewPoint(2.0, 2.0, -8.0)
        lookAt(0.0, 1.0, 0.0)
        pointLight {
            at(-1.0, 10.0, -10.0)
        }
        pointLight {
            at(5.0, 5.0, -5.0)
        }
        // floor
        +plane {
            material {
                pattern {
                    checkers(colour(1.0, 0.9, 0.9), colour(0.5, 0.4, 0.4))
                    scale(0.25)
                }
                specular(0.0)
            }
        }
        val wallMaterial = material {
            colour(1.0, 0.9, 0.9)
            specular(0.0)
        }
        // back wall
        +plane {
            material(wallMaterial)
            rotateX(-PI / 2)
            translateZ(3.0)
        }
        // left wall
        +plane {
            material(wallMaterial)
            rotateZ(-PI / 2)
            translateX(-3.0)
        }
        // left sphere
        +sphere {
            material {
                pattern {
                    stripes(colour(0.5, 1.0, 0.1), colour(0.1, 1.0, 0.5))
                    scale(0.1, 1.0, 1.0)
                    rotateZ(PI / 2.0)
                }
                diffuse(0.7)
                specular(0.3)
            }
            scale(0.33)
            translate(-1.5, 0.33, -0.75)
        }
        // middle sphere
        +sphere {
            material {
                colour(0.1, 1.0, 0.5)
                diffuse(0.7)
                specular(0.3)
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
                translate(cos(angle), 0.0, sin(angle))
            }
        }
    }


fun main() {
    PixelSourceRenderer(pixelSource(scene()))
}
