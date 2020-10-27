package com.notedgeek.rtrace.graphics

import java.io.IOException
import java.util.*
import kotlin.math.min


typealias Bound = Pair<Pair<Int, Int>, Pair<Int, Int>>

class PixelSourceRenderer(private val pixelSource: PixelSource) : Renderer {
    internal inner class RenderRunner(
        private val bounds: Deque<Bound>,
        private val renderContext: RenderContext
    ) : Runnable {
        override fun run() {
            while(true) {
                val bound: Bound
                synchronized(bounds) {
                    if (bounds.isEmpty()) {
                        return
                    } else bound = bounds.removeFirst()
                }
                for (y in bound.first.second .. bound.second.second) {
                    for (x in bound.first.first .. bound.second.first) {
                        val c = pixelSource.colorAt(x, y)
                        renderContext.setRgb(x, y, c.red, c.green, c.blue)
                    }
                }
            }
        }
    }

    override fun render(renderContext: RenderContext) {
        val width = renderContext.sizeX
        val height = renderContext.sizeY
        val squareSize = 128
        val boundsList = LinkedList<Bound>()
        var x1 = 0
        var y1 = 0
        while(true) {
            val x2 = min(x1 + squareSize - 1, width - 1)
            val y2 = min(y1 + squareSize - 1, height - 1)
            val chunk = Pair(x1 to y1, x2 to y2)
            boundsList.add(chunk)
            x1 = x2 + 1
            if(x1 >= width) {
                x1 = 0
                y1 = y2 + 1
                if(y1 >= height) {
                    break
                }
            }
        }
        val start = System.currentTimeMillis()
        val count = 8
        val threads: MutableList<Thread> = ArrayList()
        for (i in 0 until count) {
            val thread = Thread(RenderRunner(boundsList, renderContext))
            thread.start()
            threads.add(thread)
        }
        for (thread in threads) {
            try {
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        println(System.currentTimeMillis() - start)
    }

    init {
        val renderFrame = RenderFrame("pixel source renderer", pixelSource.width, pixelSource.height, this)
        renderFrame.render()
        try {
            renderFrame.writeToFile("render${System.currentTimeMillis()}.png")
        } catch (ioEx: IOException) {
            ioEx.printStackTrace()
        }
    }
}