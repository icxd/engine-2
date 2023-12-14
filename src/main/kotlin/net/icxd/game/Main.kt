package net.icxd.game

import net.icxd.engine.Engine
import net.icxd.engine.IGame
import net.icxd.engine.MouseInput
import net.icxd.engine.Window
import net.icxd.engine.graphics.Material
import net.icxd.engine.graphics.Mesh
import net.icxd.engine.graphics.Model
import net.icxd.engine.graphics.Renderer
import net.icxd.engine.scene.Entity
import net.icxd.engine.scene.Scene
import net.icxd.engine.util.ModelLoader
import org.lwjgl.glfw.GLFW.*

object Main : IGame {
    private const val MOUSE_SENSITIVITY = 0.1f
    private const val MOVEMENT_SPEED = 0.005f

    @JvmStatic
    fun main(args: Array<String>) {
        val game = Main
        val engine = Engine("Game", Window.Companion.WindowOptions.defaultOptions(), game)
        engine.start()
    }

    private lateinit var cubeEntity: Entity
    private var rotation: Float = 0f

    override fun cleanup() { }

    override fun init(window: Window, scene: Scene, renderer: Renderer) {
        val model = ModelLoader.loadModel("cube-model", "resources/models/cube/cube.obj", scene.getTextureCache())
        scene.addModel(model)

        cubeEntity = Entity("cube-entity", model.getId())
        cubeEntity.setPosition(0f, 0f, -2f)
        scene.addEntity(cubeEntity)
    }

    override fun input(window: Window, scene: Scene, diffTimeMillis: Long) {
        val move = diffTimeMillis * MOVEMENT_SPEED
        val camera = scene.getCamera()
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move)
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackward(move)
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move)
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move)
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            camera.moveUp(move)
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.moveDown(move)
        }

        val mouseInput: MouseInput = window.getMouseInput()
        if (mouseInput.isRightButtonPressed()) {
            val displVec = mouseInput.getDisplVec()
            camera.addRotation(
                Math.toRadians((displVec.x * MOUSE_SENSITIVITY).toDouble()).toFloat(),
                Math.toRadians((displVec.y * MOUSE_SENSITIVITY).toDouble()).toFloat()
            )
        }
    }

    override fun update(window: Window, scene: Scene, diffTimeMillis: Long) {
//        rotation += 1.5f;
//        if (rotation > 360) {
//            rotation = 0f;
//        }
//        cubeEntity.setRotation(1f, 1f, 1f, Math.toRadians(rotation.toDouble()).toFloat());
//        cubeEntity.updateModelMatrix();
    }
}