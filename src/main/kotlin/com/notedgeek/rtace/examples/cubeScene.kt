package com.notedgeek.rtace.examples

import com.notedgeek.rtace.closeTo
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene =
    buildScene (){
        val scale = 4
        size(3300 / scale, 1340 / scale)
        viewPoint(5.0, 5.0, -15.0)
        lookAt(0.0, 0.0, 0.0)
        pointLight {
            at(-5.0, -0.8, -5.0)
        }
        pointLight {
            at(5.0, 10.0, -10.0)
        }

        // floor
        plane {
            material {
                pattern {
                    checkers(colour(0.8, 0.6, 0.6), colour(0.4, 0.2, 0.2))
                }
                specular(0.0)
            }
            translateY(-5.0)
        }

        // back wall
        plane {
            material {
                colour(0.1, 0.0, 0.0)
                reflective(0.8)
                specular(0.0)
            }
            rotateX(-PI / 2)
            translateZ(10.0)
        }

        // left wall
        plane {
            material {
                colour(0.0, 0.0, 0.0)
                diffuse(0.2)
                reflective(0.4)
                specular(0.0)
            }
            rotateZ(-PI / 2)
            translateX(-6.0)
        }

        // front wall
        plane {
            material {
                colour(1.0, 0.9, 0.9)
                specular(0.0)
            }
            rotateX(-PI / 2)
            translateZ(-20.0)
        }

        sphere {
            scale(2.5)
            material {
                colour(0.0, 0.1, 0.0)
                transparency(0.7)
                refractiveIndex(1.08)
                reflective(0.3)
            }
        }

        val cube1 = defObject {
            cube {
                scale(0.1, 0.2, 0.1)
                material {
                    reflective(0.4)
                }
            }
        }

        val radius = 3.0
        val perQuarter = 8
        val count = perQuarter * 4
        val step = PI / (2 * perQuarter)
        for(i in 0 until count) {
            val angle = step * i
            add(cube1) {
                material {
                    colour(0.4, 0.1, 0.2)
                }
                translateY(radius)
                rotateZ(angle)
            }
            if (!closeTo((angle + PI / 2.0) % PI, 0.0)) {
                add(cube1) {
                    material {
                        colour(0.2, 0.4, 0.1)
                    }
                    translateZ(radius)
                    rotateX(angle)
                }
            }
            if (!closeTo(angle % (PI / 2.0), 0.0)) {
                add(cube1) {
                    material {
                        colour(0.1, 0.2, 0.4)
                    }
                    translateX(radius)
                    rotateY(angle)
                }
            }
        }

    }

fun main() {
    PixelSourceRenderer(pixelSource(scene))
}
