package com.notedgeek.rtrace.graphics

import java.io.File
import javax.swing.JFrame
import javax.swing.SwingUtilities

class RenderFrame(name: String?, width: Int, height: Int, renderer: Renderer) : JFrame(name) {
    private val renderer: Renderer
    private val renderContext: RenderContext
    private val panel: ImageSourceRenderContextPanel
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
        panel = ImageSourceRenderContextPanel(width, height)
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