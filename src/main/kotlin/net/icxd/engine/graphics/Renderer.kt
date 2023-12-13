package net.icxd.engine.graphics

import net.icxd.engine.Window
import net.icxd.engine.scene.Scene
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Renderer {
    private val sceneRenderer: SceneRenderer

    init {
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        sceneRenderer = SceneRenderer()
    }

    fun cleanup() = sceneRenderer.cleanup()

    fun render(window: Window, scene: Scene) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.getWidth(), window.getHeight())

        sceneRenderer.render(scene)
    }
}