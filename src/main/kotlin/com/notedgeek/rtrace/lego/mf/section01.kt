package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.EAST_13
import com.notedgeek.rtrace.lego.EAST_4_6
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
import com.notedgeek.rtrace.lego.lego
import kotlin.math.PI

val section01 = lego {

    // 1
    val p1s1 = p(TECH_SQR_RING_6_BLK)
    j(PEG_1_2_BLK, PEG_OUT, p1s1, WEST_3)

    // 2
    val p2s1 = p(TECH_BAR_16_DG, -1, 2)
    val p2s2 = p(TECH_SQR_RING_8_BLK, -7, -1)

    // 3
    val p3bm = p(TECH_BAR_14_LG, -1, -12)

    // 4
    p(PLATE_6_8_LG, -2, -1, 3)
    j(PEG_1_2_BLK, PEG_OUT, p2s2, WEST_4)

    // 5
    p(PLATE_6_8_LG, -2, -1, 4)
    val p5br = p(TECH_BAR_16_DG, -1, -28, 0)
    val p5bl = p(TECH_BAR_16_DG, -1, 18, 0)
    setZ(3)
    p(PLATE_1_4_LG, -1, -28)
    p(PLATE_2_3_LG.east(), -1, -20)
    p(PLATE_1_8_LG, -1, -16)
    p(PLATE_1_4_LG, -1, 30)
    p(PLATE_2_3_LG.east(), -1, 24)
    p(PLATE_1_8_LG, -1, 14)

    // 6
    p(PLATE_1_4_LG, -1, -28, 4)
    p(PLATE_2_3_LG.east(), -1, -20)
    p(PLATE_1_8_LG, -1, -16)
    p(PLATE_1_4_LG, -1, 30)
    p(PLATE_2_3_LG.east(), -1, 24)
    p(PLATE_1_8_LG, -1, 14)

    // 7
    setZ(5)
    val p7tm = p(TECH_BAR_14_LG, -1, -12)
    val p7tr = p(TECH_BAR_16_DG, -1, -28)
    val p7tl = p(TECH_BAR_16_DG, -1, 18)

    // 8
    val p8bl = j(PEG_BLK, PEG_IN, p5bl, WEST_15)
    j(PEG_BLK, PEG_IN, p7tl, WEST_15)
    j(PEG_BLK, PEG_IN, p3bm, WEST_1)
    val p8tm = j(PEG_BLK, PEG_IN, p7tm, WEST_1)
    j(PEG_BLK, PEG_IN, p5br, WEST_1)
    val p8tr = j(PEG_BLK, PEG_IN, p7tr, WEST_1)
    j(TECH_SQR_RING_6_BLK, NORTH_1_6, p8tm, PEG_OUT)

    // 9
    p(TECH_BAR_16_DG, 0, -8, -12)
    p(TECH_SQR_RING_6_BLK, -12)

    // 10
    p(PLATE_6_8_LG, -12, -1, 3)

    // 11
    p(PLATE_6_8_LG, -12, -1, 4)
    val p11 = lego {
        val p11sq = p(TECH_SQR_RING_6_BLK)
        p(PLATE_4_6_LG, z = 3)
        p(PLATE_2_4_LG.east(), z = 4)
        p(PLATE_2_4_LG.east(), y = 4)
        j(PEG_BLK, PEG_IN, p11sq, SOUTH_1)
        j(PEG_BLK, PEG_IN, p11sq, SOUTH_3)
    }
    j(p11, NORTH_1_6, p8tr, PEG_OUT)
    j(p11, NORTH_1_6, p8bl, PEG_OUT, PI)

    // 12
    p(-8, -28, 0) {
        p(TECH_BAR_16_DG)
        p(PLATE_1_4_LG, z = 3)
        p(PLATE_2_3_LG.west(), -2, 8)
        p(PLATE_1_8_LG, 0, 12)
        p(PLATE_1_4_LG, 0, 0, 4)
        p(PLATE_2_3_LG.west(), -2, 8)
        p(PLATE_1_8_LG, 0, 12)
        p(TECH_BAR_16_DG, 0, 0, 5)
    }

    // 13
    p(-7, -1, 5) {
        val p13sq = p(TECH_SQR_RING_8_BLK)
        j(PEG_1_2_BLK, PEG_OUT, p13sq, WEST_4)
        j(PEG_1_2_BLK, PEG_OUT, p13sq, EAST_4_6)
        p(TECH_BAR_16_DG, 6, 3)
        p(TECH_BAR_16_DG, -1, -11)
        p(TECH_SQR_RING_6_BLK, 7, 1)
        p(TECH_SQR_RING_6_BLK, -5, 1)
    }

    // 14
    val p14 = lego {
        val p14sq = p(TECH_SQR_RING_6_BLK)
        s("p14p", j(PEG_BLK, PEG_IN, p14sq, SOUTH_1))
        j(PEG_BLK, PEG_IN, p14sq, SOUTH_3)
    }
    j(p14, g("p14p"), PEG_OUT, p2s1, WEST_15, PI)

    // 15
    p(-8, 18, 0) {
        p(TECH_BAR_16_DG)
        p(TECH_BAR_14_LG, y = -14)
        p(PLATE_1_8_LG, 0, -4, 3)
        p(PLATE_2_3_LG.west(), -2, 6)
        p(PLATE_1_4_LG, 0, 12, 3)
        p(PLATE_1_8_LG, 0, -4, 4)
        p(PLATE_2_3_LG.west(), -2, 6)
        p(PLATE_1_4_LG, 0, 12)
        p(TECH_BAR_16_DG, 0, 0, 5)
    }

    // 16
    p(TECH_BAR_16_DG.east(), 0, -1, 0)
    p(TECH_BAR_16_DG.east(), y = 6)
    p(TECH_BAR_14_LG, -8, 4, 5)

    // 17
    p(16, 7, 0) {
        val s17back = lego {
            val s17sq = p(TECH_SQR_RING_6_BLK)
            p(PLATE_4_6_LG, z = 3)
            for (x in 0..1) {
                for (y in 0..5) {
                    p(STEERING_WHEEL_BASE_DG, x * 2, y, 4)
                }
            }
            j(PEG_BLK, PEG_IN, s17sq, SOUTH_1)
            j(PEG_BLK, PEG_IN, s17sq, SOUTH_3)
        }
        val s17bottomBar = p(TECH_BAR_14_LG)
        resetXYZ()
        p(PLATE_1_8_LG, y = -4, z = 3)
        p(PLATE_1_4_LG, y = 10, z = 3)
        p(PLATE_1_8_LG, y = -4, z = 4)
        p(PLATE_1_4_LG, y = 10, z = 4)
        p(TECH_BAR_14_LG, 0, 0, 5)
        p(PLATE_1_2_LG, y = 12, z = 8)
        val s17SidePeg = j(PEG_BLK, PEG_IN, s17bottomBar, EAST_13)
        j(s17back, NORTH_3_6, s17SidePeg, PEG_OUT, PI)
        resetXYZ()
        p(TECH_BAR_14_LG, x = 7)
        p(PLATE_1_8_LG, y = -4, z = 3)
        p(PLATE_1_4_LG, y = 10)
        p(PLATE_1_8_LG, y = -4, z = 4)
        p(PLATE_1_4_LG, y = 10)
        p(TECH_BAR_14_LG, y = 0, z = 5)
        p(PLATE_1_2_LG, y = 12, z = 8)
        rotateY(PI / 2)
    }
}
