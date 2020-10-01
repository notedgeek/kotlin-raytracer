package com.notedgeek.rtrace.graphics

import java.io.IOException
import java.util.*

class pixelSourceRenderer(private val pixelSource: pixelSource) : renderer {
    internal inner class RenderRunner(
        private val start: Int,
        private val inc: Int,
        private val renderContext: renderContext
    ) : Runnable {
        override fun run() {
            for (y in 0 until renderContext.sizeY) {
                var x = start
                while (x < renderContext.sizeX) {
                    val c = pixelSource.colorAt(x, y)
                    renderContext.setRgb(
                        x, y, c.red, c.green, c.blue
                    )
                    x += inc
                }
            }
        }
    }

    override fun render(renderContext: renderContext) {
        val start = System.currentTimeMillis()
        val count = 8
        val threads: MutableList<Thread> = ArrayList()
        for (i in 0 until count) {
            val thread = Thread(RenderRunner(i, count, renderContext))
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
        val renderFrame = renderFrame("pixel source renderer", pixelSource.width, pixelSource.height, this)
        renderFrame.render()
        try {
            val `val` = 0
            renderFrame.writeToFile(String.format("/Users/seye/render%d.png", `val`))
        } catch (ioex: IOException) {
            ioex.printStackTrace()
        }
    }
}