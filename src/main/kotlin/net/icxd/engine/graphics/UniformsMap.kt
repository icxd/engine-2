package net.icxd.engine.graphics

import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryStack

class UniformsMap(private val programId: Int) {
    private val uniformsMap: MutableMap<String, Int> = mutableMapOf()

    fun createUniform(uniformName: String) {
        val uniformLocation = glGetUniformLocation(programId, uniformName)
        if (uniformLocation < 0) {
            throw IllegalStateException("Could not find uniform: $uniformName")
        }
        uniformsMap[uniformName] = uniformLocation
    }

    private fun getUniformLocation(uniformName: String): Int =
        uniformsMap[uniformName] ?: throw RuntimeException("Could not find uniform $uniformName")

    fun setUniform(uniformName: String, value: Int) = glUniform1i(getUniformLocation(uniformName), value)

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            glUniformMatrix4fv(getUniformLocation(uniformName), false, value.get(stack.mallocFloat(16)))
        }
    }

    fun setUniform(uniformName: String, value: Vector4f) { glUniform4f(getUniformLocation(uniformName), value.x(), value.y(), value.z(), value.w()) }

}