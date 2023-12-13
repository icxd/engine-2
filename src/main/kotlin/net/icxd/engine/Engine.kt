package net.icxd.engine

import net.icxd.engine.graphics.Renderer
import net.icxd.engine.scene.Scene

class Engine(windowTitle: String, opts: Window.Companion.WindowOptions, val game: IGame) {
    companion object {
        const val TARGET_UPS: Int = 30
    }

    private val window: Window
    private var renderer: Renderer
    private var running: Boolean
    private var scene: Scene
    private var targetFps: Int
    private var targetUps: Int

    init {
        window = Window(windowTitle, opts) {
            resize()
            return@Window null
        }
        targetFps = opts.fps
        targetUps = opts.ups
        renderer = Renderer()
        scene = Scene(window.getWidth(), window.getHeight())
        game.init(window, scene, renderer)
        running = true;
    }

    private fun cleanup() {
        game.cleanup()
        renderer.cleanup()
        scene.cleanup()
        window.cleanup()
    }

    private fun resize() {
        scene.resize(window.getWidth(), window.getHeight())
    }

    private fun run() {
        var initialTime = System.currentTimeMillis()
        val timeU = 1000.0f / targetUps
        val timeR = if (targetFps > 0) 1000.0f / targetFps else 0f
        var deltaUpdate = 0f
        var deltaFps = 0f

        var updateTime = initialTime
        while (running && !window.windowShouldClose()) {
            window.pollEvents()

            val now = System.currentTimeMillis()
            deltaUpdate += (now - initialTime) / timeU
            deltaFps += (now - initialTime) / timeR

            if (targetFps <= 0 || deltaFps >= 1)
                game.input(window, scene, now - initialTime)

            if (deltaUpdate >= 1) {
                val diffTimeMillis = now - updateTime
                game.update(window, scene, diffTimeMillis)
                updateTime = now;
                deltaUpdate--
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                renderer.render(window, scene)
                deltaFps--
                window.update()
            }

            initialTime = now
        }
        cleanup()
    }

    fun start() = run()
    fun stop() { running = false }
}