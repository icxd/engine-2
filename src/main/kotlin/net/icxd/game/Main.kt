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
import org.lwjgl.glfw.GLFW.*
import kotlin.properties.Delegates


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
        val positions = floatArrayOf(
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
        )
        val textCoords = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
        )
        val indices = intArrayOf(
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,
        )
        val texture = scene.getTextureCache().createTexture("resources/models/cube.png")
        val material = Material()
        material.setTexturePath(texture.getTexturePath())
        val materialList: MutableList<Material> = mutableListOf()
        materialList.add(material)

        val mesh = Mesh(positions, textCoords, indices)
        material.getMeshList().add(mesh)
        val model = Model("cube-model", materialList)
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