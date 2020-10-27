package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.obj.Sphere
import com.notedgeek.rtrace.sceneBuilder.buildGroup
import kotlin.math.PI

private const val RIDGE_WIDTH = 0.3 * SCALE
private const val RIDGE_HEIGHT = 0.3 * SCALE
private const val SLIT_DEPTH = 3.5 * SCALE
private const val SLIT_WIDTH = 0.8 * SCALE
private const val SECOND_SLIT_DEPTH = 7 * SCALE

val pegHalf = buildGroup {

    +difference {
        +union {
            +difference {
                +cappedCylinder {
                    scale(TECH_SHOULDER_RADIUS, BRICK_WIDTH, TECH_SHOULDER_RADIUS)
                }
                +difference {
                    +cappedCylinder {
                        scale(TECH_SHOULDER_RADIUS * 1.01, BRICK_WIDTH, TECH_SHOULDER_RADIUS * 1.01)
                    }
                    +cappedCylinder {
                        scale(TECH_HOLE_RADIUS, BRICK_WIDTH * 1.01, TECH_HOLE_RADIUS)
                        translateY(-0.005)
                    }
                    translateY(TECH_SHOULDER_DEPTH * 0.6)
                }
            }
            +cappedCylinder {
                scale(TECH_HOLE_RADIUS + RIDGE_WIDTH, RIDGE_HEIGHT, TECH_HOLE_RADIUS + RIDGE_WIDTH)
                translateY(BRICK_WIDTH - RIDGE_HEIGHT)
            }
        }
        +cappedCylinder {
            scale(TECH_PEG_INNER_HOLE_RADIUS, BRICK_WIDTH * 1.01, TECH_PEG_INNER_HOLE_RADIUS)
            translateY(-0.005)
        }
        +cube {
            scale(TECH_SHOULDER_RADIUS * 1.1, SLIT_DEPTH / 2, SLIT_WIDTH / 2)
            translateY(SLIT_DEPTH / 2 + BRICK_WIDTH - SLIT_DEPTH + 0.001)
        }
    }

}

val longPegHalf = buildGroup {
    +difference {
        +union {
            +difference {
                +cappedCylinder {
                    scale(TECH_SHOULDER_RADIUS, BRICK_WIDTH * 2, TECH_SHOULDER_RADIUS)
                }
                +difference {
                    +cappedCylinder {
                        scale(TECH_SHOULDER_RADIUS * 1.01, BRICK_WIDTH * 2, TECH_SHOULDER_RADIUS * 1.01)
                    }
                    +cappedCylinder {
                        scale(TECH_HOLE_RADIUS, BRICK_WIDTH * 2.001, TECH_HOLE_RADIUS)
                        translateY(-0.0005)
                    }
                    translateY(TECH_SHOULDER_DEPTH * 0.6)
                }
            }
            +cappedCylinder {
                scale(TECH_HOLE_RADIUS + RIDGE_WIDTH, RIDGE_HEIGHT, TECH_HOLE_RADIUS + RIDGE_WIDTH)
                translateY(BRICK_WIDTH * 2 - RIDGE_HEIGHT)
            }
            +cappedCylinder {
                scale(TECH_HOLE_RADIUS + RIDGE_WIDTH, RIDGE_HEIGHT, TECH_HOLE_RADIUS + RIDGE_WIDTH)
                translateY(BRICK_WIDTH - RIDGE_HEIGHT)
            }
        }
        +cappedCylinder {
            scale(TECH_PEG_INNER_HOLE_RADIUS, BRICK_WIDTH * 2.01, TECH_PEG_INNER_HOLE_RADIUS)
            translateY(-0.005)
        }
        +cube {
            scale(TECH_SHOULDER_RADIUS * 1.1, SLIT_DEPTH / 2, SLIT_WIDTH / 2)
            translateY(SLIT_DEPTH / 2 + BRICK_WIDTH * 2 - SLIT_DEPTH + 0.001)
        }
    }
}

val pegSlit = buildGroup {
    +cube {
        scale(TECH_SHOULDER_RADIUS * 1.1, SECOND_SLIT_DEPTH / 2, SLIT_WIDTH / 2)
        translateY(SECOND_SLIT_DEPTH / 2)
        rotateY(PI / 2)
    }
}


val peg = buildGroup {
    +difference {
        +union {
            +from(pegHalf)
            +from(pegHalf) {
                rotateX(PI)
            }
        }
        +from(pegSlit) {
            translateY(-SECOND_SLIT_DEPTH / 2)
        }
    }
    translateY(BRICK_WIDTH)
}


val pegOneTwo = buildGroup {
    +difference {
        +union {
            +from(longPegHalf)
            +from(pegHalf) {
                rotateX(PI)
            }
        }
        +from(pegSlit) {
            translateY(BRICK_WIDTH - SECOND_SLIT_DEPTH / 2)
        }
        +from(pegSlit) {
            translateY(-SECOND_SLIT_DEPTH / 2)
        }
    }
    translateY(BRICK_WIDTH)
}

val test = buildGroup {
    +difference {
        +sphere { }
        +from(Sphere()) {
            translateX(-.75)
        }
        +from(Sphere()) {
            translateX(.575)
        }
    }
}

