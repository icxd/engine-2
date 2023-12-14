package net.icxd.engine

import net.icxd.engine.util.Logger
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.*
import java.util.concurrent.Callable

open class Window(title: String, opts: WindowOptions, private val resizeFunc: Callable<Void>) {
    private var windowHandle: Long
    private var width: Int
    private var height: Int
    private val mouseInput: MouseInput

    init {
        if (!glfwInit()) throw IllegalStateException("Unable to initialize GLFW")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        if (opts.compatibleProfile) glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
        else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        }

        if (opts.width > 0 && opts.height > 0) {
            this.width = opts.width
            this.height = opts.height
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)
            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor()) ?: throw RuntimeException("Failed to get video mode")
            this.width = vidMode.width()
            this.height = vidMode.height()
        }

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (windowHandle == NULL) throw RuntimeException("Failed to create the GLFW window")

        glfwSetFramebufferSizeCallback(windowHandle) { _, width, height -> resized(width, height) }
        glfwSetErrorCallback() { error, msg -> Logger.error("Error code [$error]: ${memUTF8(msg)}") }
        glfwSetKeyCallback(windowHandle) { _, key, _, action, _ -> keyCallback(key, action) }

        glfwMakeContextCurrent(windowHandle)

        glfwSwapInterval(if (opts.fps > 0) 0 else 1)

        glfwShowWindow(windowHandle)

        val arrWidth = IntArray(1)
        val arrHeight = IntArray(1)
        glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight)
        width = arrWidth[0]
        height = arrHeight[0]

        mouseInput = MouseInput(windowHandle)
    }

    fun cleanup() {
        glfwFreeCallbacks(windowHandle)
        glfwDestroyWindow(windowHandle)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun keyCallback(key: Int, action: Int) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(windowHandle, true)
    }

    private fun resized(width: Int, height: Int) {
        this.width = width
        this.height = height
        try { resizeFunc.call() }
        catch (e: Exception) { Logger.error("Error resizing window: ${e.message}") }
    }

    fun pollEvents() = glfwPollEvents()
    fun isKeyPressed(keycode: Int): Boolean = glfwGetKey(windowHandle, keycode) == GLFW_PRESS
    fun update() = glfwSwapBuffers(windowHandle)
    fun windowShouldClose(): Boolean = glfwWindowShouldClose(windowHandle)

    companion object {
        class WindowOptions(
            val compatibleProfile: Boolean,
            val fps: Int,
            val height: Int,
            val width: Int,
            val ups: Int = Engine.TARGET_UPS,
        ) {
            companion object {
                fun defaultOptions() = WindowOptions(false, 60, 0, 0)
            }
        }
    }

    fun getWidth(): Int = width
    fun getHeight(): Int = height
    fun getMouseInput(): MouseInput = mouseInput
}