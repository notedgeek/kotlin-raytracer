package com.notedgeek.rtace.examples

import com.notedgeek.rtace.sceneBuilder.buildScene
import kotlin.math.PI

val backdrop1 = buildScene {
    size(3300 / 1, 1340 / 1)
    viewPoint(4.0, 6.0, -9.0)
    lookAt(-1.0, 1.0, 0.0)
    pointLight {
        at(-1.0, 10.0, -9.0)
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
    // front wall
    +plane {
        material(wallMaterial)
        rotateX(PI / 2)
        translateZ(-10.0)
    }
    // left wall
    +plane {
        material {
            colour(0.0, 0.0, 0.0)
            diffuse(0.2)
            reflective(0.4)
        }
        rotateZ(-PI / 2)
        translateX(-3.0)
    }
}