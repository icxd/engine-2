package net.icxd.engine.graphics

import net.icxd.engine.util.File
import org.lwjgl.opengl.GL30.*

open class ShaderProgram(val shaderModuleDataList: List<ShaderModuleData>) {
    private val programId: Int = glCreateProgram()

    init {
        if (programId == 0) throw RuntimeException("Could not create Shader")

        val shaderModules = shaderModuleDataList.map { s -> createShader(File.readFile(s.shaderFile), s.shaderType) }
        link(shaderModules)
    }

    fun bind() = glUseProgram(programId)
    fun unbind() = glUseProgram(0)

    fun cleanup() {
        unbind()
        if (programId != 0) glDeleteProgram(programId)
    }

    private fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) throw RuntimeException("Error creating shader. Type: $shaderType")

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) throw RuntimeException("Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}")

        glAttachShader(programId, shaderId)

        return shaderId
    }

    fun getProgramId(): Int = programId

    private fun link(shaderModules: List<Int>) {
        glLinkProgram(programId)
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) throw RuntimeException("Error linking Shader code: ${glGetProgramInfoLog(programId, 1024)}")

        shaderModules.forEach { glDetachShader(programId, it) }
        shaderModules.forEach(::glDeleteShader)
    }

    fun validate() {
        glValidateProgram(programId)
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)
            throw RuntimeException("Warning validating Shader code: ${glGetProgramInfoLog(programId, 1024)}")
    }

    data class ShaderModuleData(val shaderFile: String, val shaderType: Int)
}