package net.icxd.game

import net.icxd.engine.Engine
import net.icxd.engine.IGame
import net.icxd.engine.Window
import net.icxd.engine.graphics.Mesh
import net.icxd.engine.graphics.Model
import net.icxd.engine.graphics.Renderer
import net.icxd.engine.scene.Entity
import net.icxd.engine.scene.Scene
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*

object Main : IGame {
    private lateinit var cubeEntity: Entity
    private val displInc = Vector4f()
    private var rotation: Float = 0f

    @JvmStatic
    fun main(args: Array<String>) {
        val game = Main
        val engine = Engine("Game", Window.Companion.WindowOptions.defaultOptions(), game)
        engine.start()
    }

    override fun cleanup() { }

    override fun init(window: Window, scene: Scene, renderer: Renderer) {
        val positions = floatArrayOf( // VO
            -0.5f, 0.5f, 0.5f,  // V1
            -0.5f, -0.5f, 0.5f,  // V2
            0.5f, -0.5f, 0.5f,  // V3
            0.5f, 0.5f, 0.5f,  // V4
            -0.5f, 0.5f, -0.5f,  // V5
            0.5f, 0.5f, -0.5f,  // V6
            -0.5f, -0.5f, -0.5f,  // V7
            0.5f, -0.5f, -0.5f
        )
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f
        )
        val indices = intArrayOf( // Front face
            0, 1, 3, 3, 1, 2,  // Top Face
            4, 0, 3, 5, 4, 3,  // Right face
            3, 2, 7, 5, 3, 7,  // Left face
            6, 1, 0, 6, 0, 4,  // Bottom face
            2, 1, 6, 2, 6, 7,  // Back face
            7, 6, 4, 7, 4, 5
        )
        val meshList: MutableList<Mesh> = mutableListOf()
        val mesh = Mesh(positions, colors, indices)
        meshList.add(mesh)
        val cubeModelId = "cube"
        val model = Model(cubeModelId, meshList)
        scene.addModel(model)

        cubeEntity = Entity("cube-entity", cubeModelId)
        cubeEntity.setPosition(0f, 0f, -2f)
        scene.addEntity(cubeEntity)
    }

    override fun input(window: Window, scene: Scene, diffTimeMillis: Long) {
       displInc.zero();
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displInc.y = 1f;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displInc.y = -1f;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displInc.x = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displInc.x = 1f;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            displInc.z = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displInc.z = 1f;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            displInc.w = -1f;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            displInc.w = 1f;
        }

        displInc.mul(diffTimeMillis / 1000.0f);

        val entityPos = cubeEntity.getPosition();
        cubeEntity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
        cubeEntity.setScale(cubeEntity.getScale() + displInc.w);
        cubeEntity.updateModelMatrix();
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