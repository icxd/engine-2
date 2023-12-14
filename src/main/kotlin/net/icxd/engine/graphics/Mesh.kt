package net.icxd.engine.graphics

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Mesh(positions: FloatArray, colors: FloatArray, indices: IntArray) {
    private val numVertices = indices.size
    private val vaoId: Int
    private val vboIdList: MutableList<Int> = mutableListOf()

    init {
       val stack = MemoryStack.stackPush()

        vaoId = glGenVertexArrays()
        glBindVertexArray(vaoId)

        // Position VBO
        var vboId = glGenBuffers()
        vboIdList.add(vboId)
        val positionsBuffer: FloatBuffer = stack.callocFloat(positions.size)
        positionsBuffer.put(0, positions)
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        // Color VBO
        vboId = glGenBuffers()
        vboIdList.add(vboId)
        val colorsBuffer: FloatBuffer = stack.callocFloat(colors.size)
        colorsBuffer.put(0, colors)
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        // Index VBO
        vboId = glGenBuffers()
        vboIdList.add(vboId)
        val indicesBuffer: IntBuffer = stack.callocInt(indices.size)
        indicesBuffer.put(0, indices)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers)
        glDeleteVertexArrays(vaoId)
    }

    fun getNumVertices(): Int = numVertices
    fun getVaoId(): Int = vaoId
}