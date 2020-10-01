package com.notedgeek.rtrace.graphics

import java.io.File
import javax.swing.JFrame
import javax.swing.SwingUtilities

class renderFrame(name: String?, width: Int, height: Int, renderer: renderer) : JFrame(name) {
    private val renderer: renderer
    private val renderContext: renderContext
    private val panel: imageSourceRenderContextPanel
    fun render() {
        renderer.render(renderContext)
    }

    fun writeToFile(filename: String) {
        panel.writeToFile(File(filename))
    }

    init {
        this.setSize(width, height)
        this.renderer = renderer
        val content = contentPane
        panel = imageSourceRenderContextPanel(width, height)
        renderContext = panel
        content.add(panel)
        pack()
        isVisible = true
        Thread {
            while (true) {
                try {
                    SwingUtilities.invokeAndWait { panel.repaint() }
                    Thread.sleep(1000 / 10.toLong())
                } catch (ignore: Exception) {
                }
            }
        }.start()
    }
}