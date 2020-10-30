package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.EAST_13
import com.notedgeek.rtrace.lego.EAST_4_6
import com.notedgeek.rtrace.lego.LegoContext
import com.notedgeek.rtrace.lego.NORTH_1_6
import com.notedgeek.rtrace.lego.NORTH_3_6
import com.notedgeek.rtrace.lego.PEG_IN
import com.notedgeek.rtrace.lego.PEG_OUT
import com.notedgeek.rtrace.lego.SOUTH_1
import com.notedgeek.rtrace.lego.SOUTH_3
import com.notedgeek.rtrace.lego.WEST_1
import com.notedgeek.rtrace.lego.WEST_15
import com.notedgeek.rtrace.lego.WEST_3
import com.notedgeek.rtrace.lego.WEST_4
import kotlin.math.PI

val section01 = LegoContext().apply {

    +lego {

        // 1
        val p1s1 = +TECH_SQR_RING_6_BLK
        +from(PEG_1_2_BLK) {
            join(PEG_OUT, p1s1, WEST_3)
        }

        // 2
        val p2s1 = place(TECH_BAR_16_DG, -1, 2)
        val p2s2 = place(TECH_SQR_RING_8_BLK, -7, -1)

        // 3
        val p3bm = place(TECH_BAR_14_LG, -1, -12)

        // 4
        place(PLATE_6_8_LG, -2, -1, 3)
        +from(PEG_1_2_BLK) {
            join(PEG_OUT, p2s2, WEST_4)
        }

        // 5
        place(PLATE_6_8_LG, -2, -1, 4)
        val p5br = place(TECH_BAR_16_DG, -1, -28, 0)
        val p5bl = place(TECH_BAR_16_DG, -1, 18, 0)
        setZ(3)
        place(PLATE_1_4_LG, -1, -28)
        place(PLATE_2_3_LG.east(), -1, -20)
        place(PLATE_1_8_LG, -1, -16)
        place(PLATE_1_4_LG, -1, 30)
        place(PLATE_2_3_LG.east(), -1, 24)
        place(PLATE_1_8_LG, -1, 14)

        // 6
        place(PLATE_1_4_LG, -1, -28, 4)
        place(PLATE_2_3_LG.east(), -1, -20)
        place(PLATE_1_8_LG, -1, -16)
        place(PLATE_1_4_LG, -1, 30)
        place(PLATE_2_3_LG.east(), -1, 24)
        place(PLATE_1_8_LG, -1, 14)

        // 7
        setZ(5)
        val p7tm = place(TECH_BAR_14_LG, -1, -12)
        val p7tr = place(TECH_BAR_16_DG, -1, -28)
        val p7tl = place(TECH_BAR_16_DG, -1, 18)

        // 8
        val p8bl = +from(PEG_BLK) {
            join(PEG_IN, p5bl, WEST_15)
        }
        +from(PEG_BLK) {
            join(PEG_IN, p7tl, WEST_15)
        }
        +from(PEG_BLK) {
            join(PEG_IN, p3bm, WEST_1)
        }
        val p8tm = +from(PEG_BLK) {
            join(PEG_IN, p7tm, WEST_1)
        }
        +from(PEG_BLK) {
            join(PEG_IN, p5br, WEST_1)
        }
        val p8tr = +from(PEG_BLK) {
            join(PEG_IN, p7tr, WEST_1)
        }
        +from(TECH_SQR_RING_6_BLK) {
            join(NORTH_1_6, p8tm, PEG_OUT)
        }

        // 9
        place(TECH_BAR_16_DG, 0, -8, -12)
        place(TECH_SQR_RING_6_BLK, -12)

        // 10
        place(PLATE_6_8_LG, -12, -1, 3)

        // 11
        place(PLATE_6_8_LG, -12, -1, 4)
        val p11 = lego {
            val p11sq = place(TECH_SQR_RING_6_BLK)
            place(PLATE_4_6_LG, z = 3)
            place(PLATE_2_4_LG.east(), z = 4)
            place(PLATE_2_4_LG.east(), y = 4)
            +from(PEG_BLK) {
                join(PEG_IN, p11sq, SOUTH_1)
            }
            +from(PEG_BLK) {
                join(PEG_IN, p11sq, SOUTH_3)
            }
        }
        +from(p11) {
            join(NORTH_1_6, p8tr, PEG_OUT)
        }
        +from(p11) {
            join(NORTH_1_6, p8bl, PEG_OUT, PI)
        }

        // 12
        place(-8, -28, 0) {
            place(TECH_BAR_16_DG)
            place(PLATE_1_4_LG, z = 3)
            place(PLATE_2_3_LG.west(), -2, 8)
            place(PLATE_1_8_LG, 0, 12)
            place(PLATE_1_4_LG, 0, 0, 4)
            place(PLATE_2_3_LG.west(), -2, 8)
            place(PLATE_1_8_LG, 0, 12)
            place(TECH_BAR_16_DG, 0, 0, 5)
        }

        // 13
        place(-7, -1, 5) {
            val p13sq = place(TECH_SQR_RING_8_BLK)
            +from(PEG_1_2_BLK) {
                join(PEG_OUT, p13sq, WEST_4)
            }
            +from(PEG_1_2_BLK) {
                join(PEG_OUT, p13sq, EAST_4_6)
            }
            place(TECH_BAR_16_DG, 6, 3)
            place(TECH_BAR_16_DG, -1, -11)
            place(TECH_SQR_RING_6_BLK, 7, 1)
            place(TECH_SQR_RING_6_BLK, -5, 1)
        }

        // 14
        val p14 = lego {
            val p14sq = place(TECH_SQR_RING_6_BLK)
            put("p14p", +from(PEG_BLK) {
                join(PEG_IN, p14sq, SOUTH_1)
            })
            +from(PEG_BLK) {
                join(PEG_IN, p14sq, SOUTH_3)
            }
        }
        val p14p = get("p14p")
        +from(p14) {
            join(p14p, PEG_OUT, p2s1, WEST_15, PI)
        }

        // 15
        place(-8, 18, 0) {
            place(TECH_BAR_16_DG)
            place(TECH_BAR_14_LG, y = -14)
            place(PLATE_1_8_LG, 0, -4, 3)
            place(PLATE_2_3_LG.west(), -2, 6)
            place(PLATE_1_4_LG, 0, 12, 3)
            place(PLATE_1_8_LG, 0, -4, 4)
            place(PLATE_2_3_LG.west(), -2, 6)
            place(PLATE_1_4_LG, 0, 12)
            place(TECH_BAR_16_DG, 0, 0, 5)
        }

        // 16
        place(TECH_BAR_16_DG.east(), 0, -1, 0)
        place(TECH_BAR_16_DG.east(), y = 6)
        place(TECH_BAR_14_LG, -8, 4, 5)

        // 17
        place(16, 7, 0) {
            val s17back = lego {
                val s17sq = place(TECH_SQR_RING_6_BLK)
                place(PLATE_4_6_LG, z = 3)
                for (x in 0..1) {
                    for (y in 0..5) {
                        place(STEERING_WHEEL_BASE_DG, x * 2, y, 4)
                    }
                }
                +from(PEG_BLK) {
                    join(PEG_IN, s17sq, SOUTH_1)
                }
                +from(PEG_BLK) {
                    join(PEG_IN, s17sq, SOUTH_3)
                }
            }
            val s17bottomBar = place(TECH_BAR_14_LG)
            resetXYZ()
            place(PLATE_1_8_LG, y = -4, z = 3)
            place(PLATE_1_4_LG, y = 10, z = 3)
            place(PLATE_1_8_LG, y = -4, z = 4)
            place(PLATE_1_4_LG, y = 10, z = 4)
            place(TECH_BAR_14_LG, 0, 0, 5)
            place(PLATE_1_2_LG, y = 12, z = 8)
            val s17SidePeg = +from(PEG_BLK) {
                join(PEG_IN, s17bottomBar, EAST_13)
            }
            +from(s17back) {
                join(NORTH_3_6, s17SidePeg, PEG_OUT, PI)
            }
            resetXYZ()
            place(TECH_BAR_14_LG, x = 7)
            place(PLATE_1_8_LG, y = -4, z = 3)
            place(PLATE_1_4_LG, y = 10)
            place(PLATE_1_8_LG, y = -4, z = 4)
            place(PLATE_1_4_LG, y = 10)
            place(TECH_BAR_14_LG, y = 0, z = 5)
            place(PLATE_1_2_LG, y = 12, z = 8)
            rotateY(PI / 2)
        }
    }

}.toGroup()