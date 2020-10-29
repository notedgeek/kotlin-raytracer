package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.Material
import com.notedgeek.rtrace.WHITE

val basicPlastic = Material().withAmbient(0.2).withShininess(300.0).withReflective(0.0)

val LG = basicPlastic.withColour(WHITE * 0.8)
val DG = basicPlastic.withColour(WHITE * 0.4)
val BLK = basicPlastic.withColour(WHITE * 0.2)
