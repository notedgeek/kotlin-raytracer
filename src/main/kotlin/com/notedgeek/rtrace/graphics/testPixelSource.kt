package com.notedgeek.rtrace.graphics

import java.awt.Color

private const val squareSize = 20

fun main() {
    pixelSourceRenderer(pixelSource(1920, 1080){
        x: Int, y: Int ->
            val squareX = x / squareSize
            val squareY = y / squareSize
            if ((squareX + squareY) % 2 == 0) Color.BLUE else Color.WHITE
    })
}

