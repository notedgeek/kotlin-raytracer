package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.BLK
import com.notedgeek.rtrace.lego.DG
import com.notedgeek.rtrace.lego.LG
import com.notedgeek.rtrace.lego.pegOneTwo
import com.notedgeek.rtrace.lego.plate
import com.notedgeek.rtrace.lego.techBar
import com.notedgeek.rtrace.lego.techSquareRing

val PEG_1_2_BLK = pegOneTwo.withMaterial(BLK)
val TECH_SQR_RING_6_BLK = techSquareRing(6).withMaterial(BLK)
val TECH_SQR_RING_8_BLK = techSquareRing(8).withMaterial(BLK)
val TECH_BAR_16_DG = techBar(16).withMaterial(DG)
val TECH_BAR_14_LG = techBar(14).withMaterial(LG)
val PLATE_6_8_LG = plate(6, 8).withMaterial(LG)