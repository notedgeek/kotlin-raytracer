package com.notedgeek.rtrace.lego.mf

import com.notedgeek.rtrace.lego.EAST_3
import com.notedgeek.rtrace.lego.LegoContext
import com.notedgeek.rtrace.lego.PEG_OUT

val section01 = LegoContext().apply {

    +lego {
        val p1 = TECH_SQR_RING_6_BLK
        +p1
        val p2 = PEG_1_2_BLK
        +from(p2) {
            join(PEG_OUT, p1, EAST_3)
        }
    }

    +lego {
        place(TECH_BAR_16_DG, -1, 2)
        place(TECH_SQR_RING_8_BLK, -7, -1)
    }

    +lego {
        place(TECH_BAR_14_LG, -1, -12)
    }

    +lego {
        place(PLATE_6_8_LG, -2, -1, 3)
    }

}.toGroup()