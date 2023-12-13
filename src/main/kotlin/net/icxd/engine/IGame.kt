package net.icxd.engine

import net.icxd.engine.graphics.Renderer
import net.icxd.engine.scene.Scene

interface IGame {
    fun cleanup()
    fun init(window: Window, scene: Scene, renderer: Renderer)
    fun input(window: Window, scene: Scene, diffTimeMillis: Long)
    fun update(window: Window, scene: Scene, diffTimeMillis: Long)
}