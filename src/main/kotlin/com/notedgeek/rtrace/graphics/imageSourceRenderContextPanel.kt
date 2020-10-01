package com.notedgeek.rtrace.graphics

import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JPanel

class imageSourceRenderContextPanel(override var sizeX: Int, override var sizeY: Int) : renderContext, JPanel() {

    private val data = IntArray(sizeX * sizeY)

    init {
        preferredSize = Dimension(sizeX, sizeY)
    }

    override fun setRgb(x: Int, y: Int, r: Int, g: Int, b: Int) {
        if (r > 255 || g > 255 || b > 255) println("overflow")
        val loc = y * sizeX + x
        val alpha = 255
        data[loc] = alpha shl 24 or (0xff and r shl 16) or (0xff and g shl 8) or (0xff and b)
    }

    fun writeToFile(file: File?) {
        val bi = BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB)
        val g2 = bi.createGraphics()
        paint(g2)
        ImageIO.write(bi, "png", file)
    }

    override fun paint(graphics: Graphics) {
        graphics.drawImage(createImage(MemoryImageSource(sizeX, sizeY, data, 0, sizeX)), 0, 0, sizeX, sizeY, null)
    }

}