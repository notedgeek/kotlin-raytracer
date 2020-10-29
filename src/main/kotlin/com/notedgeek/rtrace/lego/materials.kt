package com.notedgeek.rtrace.lego

import com.notedgeek.rtrace.Colour
import com.notedgeek.rtrace.Material

val basicPlastic = Material().withAmbient(0.2).withShininess(300.0).withReflective(0.0)

val LG = basicPlastic.withColour(Colour(0.4, 0.4, 0.4))
val DG = basicPlastic.withColour(Colour(0.2, 0.2, 0.2))
val BLK = basicPlastic.withColour(Colour(0.1, 0.1, 0.1))
