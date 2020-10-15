package com.notedgeek.rtace.examples

import com.notedgeek.rtace.BLACK
import com.notedgeek.rtace.closeTo
import com.notedgeek.rtace.pixelSource
import com.notedgeek.rtace.sceneBuilder.buildScene
import com.notedgeek.rtrace.graphics.PixelSourceRenderer
import kotlin.math.PI

private val scene =
    buildScene (){
        val scale = 1
        size(3300 / scale, 1340 / scale)
        viewPoint(5.0, 5.0, -15.0)
        lookAt(0.0, 0.0, 0.0)
        pointLight {
            at(-5.0, -2.0, -5.0)
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
                colour(1.0, 0.9, 0.9)
                specular(0.0)
            }
            rotateX(-PI / 2)
            translateZ(15.0)
        }

        // left wall
        plane {
            material {
                colour(0.0, 0.0, 0.0)
                diffuse(0.2)
                reflective(0.4)
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
            scale(2.0)
            material {
                colour(BLACK)
                reflective(0.9)
            }
        }

        val cube1 = def {
            cube {
                scale(0.18)
            }
        }

        val radius = 3.0
        val perQuarter = 6
        val shinyMod = 3
        val count = perQuarter * 4
        val step = PI / (2 * perQuarter)
        for(i in 0 until count) {
            val angle = step * i
            add(cube1) {
                if (i % shinyMod == 0) {
                    material {
                        colour(BLACK)
                        reflective(0.9)
                    }
                } else {
                    material {
                        colour(0.8, 0.2, 0.4)
                    }
                }
                translateY(radius)
                rotateZ(angle)
            }
            if (!closeTo((angle + PI / 2.0) % PI, 0.0)) {
                add(cube1) {
                    if (i % shinyMod == 0) {
                        material {
                            colour(BLACK)
                            reflective(0.9)
                        }
                    } else {
                        material {
                            colour(0.4, 0.8, 0.2)
                        }
                    }
                    translateZ(radius)
                    rotateX(angle)
                }
            }
            if (!closeTo(angle % (PI / 2.0), 0.0)) {
                add(cube1) {
                    if (i % shinyMod == 0) {
                        material {
                            colour(BLACK)
                            reflective(0.9)
                        }
                    } else {
                        material {
                            colour(0.2, 0.4, 0.8)
                        }
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
