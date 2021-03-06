package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.BLK
import com.notedgeek.rtrace.lego.DG
import com.notedgeek.rtrace.lego.LG
import com.notedgeek.rtrace.lego.peg
import com.notedgeek.rtrace.lego.pegOneTwo
import com.notedgeek.rtrace.lego.plate
import com.notedgeek.rtrace.lego.steeringWheelBase
import com.notedgeek.rtrace.lego.techBar
import com.notedgeek.rtrace.lego.techSquareRing

val PEG_1_2_BLK = pegOneTwo.withMaterial(BLK)
val TECH_SQR_RING_6_BLK = techSquareRing(6).withMaterial(BLK)
val TECH_SQR_RING_8_BLK = techSquareRing(8).withMaterial(BLK)
val TECH_BAR_16_DG = techBar(16).withMaterial(DG)
val TECH_BAR_14_LG = techBar(14).withMaterial(LG)
val PLATE_6_8_LG = plate(6, 8).withMaterial(LG)
val PLATE_4_6_LG = plate(4, 6).withMaterial(LG)
val PLATE_1_4_LG = plate(1, 4).withMaterial(LG)
val PLATE_1_2_LG = plate(1, 2).withMaterial(LG)
val PLATE_1_8_LG = plate(1, 8).withMaterial(LG)
val PLATE_2_3_LG = plate(2, 3).withMaterial(LG)
val PLATE_2_4_LG = plate(2, 4).withMaterial(LG)
val PEG_BLK = peg.withMaterial(BLK)
val STEERING_WHEEL_BASE_DG = steeringWheelBase().withMaterial(DG)
