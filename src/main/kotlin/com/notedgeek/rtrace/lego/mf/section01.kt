package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.LegoContext
import com.notedgeek.rtrace.lego.PEG_OUT
import com.notedgeek.rtrace.lego.WEST_3
import com.notedgeek.rtrace.lego.WEST_4

val section01 = LegoContext().apply {


    +lego {
        // 1
        val p1s1 = +TECH_SQR_RING_6_BLK
        +from(PEG_1_2_BLK) {
            join(PEG_OUT, p1s1, WEST_3)
        }

        // 2
        place(TECH_BAR_16_DG, -1, 2)
        val p2s1 = place(TECH_SQR_RING_8_BLK, -7, -1)

        // 3
        place(TECH_BAR_14_LG, -1, -12)

        // 4
        place(PLATE_6_8_LG, -2, -1, 3)
        +from(PEG_1_2_BLK) {
            join(PEG_OUT, p2s1, WEST_4)
        }

        // 5
        place(PLATE_6_8_LG, -2, -1, 4)
        place(TECH_BAR_16_DG, -1, -28)
        place(TECH_BAR_16_DG, -1, 18)
        place(PLATE_1_4_LG, -1, -28, 3)
        place(PLATE_2_3_LG.east(), -1, -20, 3)
        place(PLATE_1_8_LG, -1, -16, 3)
        place(PLATE_1_4_LG, -1, 30, 3)
        place(PLATE_2_3_LG.east(), -1, 24, 3)
        place(PLATE_1_8_LG, -1, 14, 3)

        // 6
        place(PLATE_1_4_LG, -1, -28, 4)
        place(PLATE_2_3_LG.east(), -1, -20, 4)
        place(PLATE_1_8_LG, -1, -16, 4)
        place(PLATE_1_4_LG, -1, 30, 4)
        place(PLATE_2_3_LG.east(), -1, 24, 4)
        place(PLATE_1_8_LG, -1, 14, 4)

        // 7
        place(TECH_BAR_14_LG, -1, -12, 5)
        place(TECH_BAR_16_DG, -1, -28, 5)
        place(TECH_BAR_16_DG, -1, 18, 5)

    }

}.toGroup()