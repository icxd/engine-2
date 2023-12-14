package net.icxd.engine

import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*


class MouseInput(private val windowHandle: Long) {
    private val currentPos: Vector2f = Vector2f()
    private val displVec: Vector2f = Vector2f()
    private var inWindow = false
    private var leftButtonPressed = false
    private val previousPos: Vector2f = Vector2f(-1f, -1f)
    private var rightButtonPressed = false

    init {
        glfwSetCursorPosCallback(windowHandle) { _: Long, x: Double, y: Double ->
            currentPos.x = x.toFloat()
            currentPos.y = y.toFloat()
        }
        glfwSetCursorEnterCallback(windowHandle) { _: Long, entered: Boolean -> inWindow = entered }
        glfwSetMouseButtonCallback(windowHandle) { _: Long, button: Int, action: Int, _: Int ->
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS
        }
    }

    fun getCurrentPos(): Vector2f = currentPos
    fun getDisplVec(): Vector2f = displVec

    fun input() {
        displVec.x = 0f
        displVec.y = 0f
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            val deltax = (currentPos.x - previousPos.x).toDouble()
            val deltay = (currentPos.y - previousPos.y).toDouble()
            val rotateX = deltax != 0.0
            val rotateY = deltay != 0.0
            if (rotateX) { displVec.y = deltax.toFloat() }
            if (rotateY) { displVec.x = deltay.toFloat() }
        }
        previousPos.x = currentPos.x
        previousPos.y = currentPos.y
    }

    fun isLeftButtonPressed(): Boolean = leftButtonPressed
    fun isRightButtonPressed(): Boolean = rightButtonPressed
}