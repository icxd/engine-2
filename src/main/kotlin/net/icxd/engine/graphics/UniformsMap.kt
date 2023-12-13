package net.icxd.engine.graphics

import org.joml.Matrix4f
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

    fun setUniform(uniformName: String, value: Matrix4f) {
        MemoryStack.stackPush().use { stack ->
            glUniformMatrix4fv(uniformsMap[uniformName]!!, false, value.get(stack.mallocFloat(16)))
        }
    }

}