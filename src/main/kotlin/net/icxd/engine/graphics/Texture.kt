package net.icxd.engine.graphics

import org.lwjgl.opengl.GL30.*
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.nio.*
import kotlin.properties.Delegates

class Texture {
    private var textureId by Delegates.notNull<Int>()
    private val texturePath: String

    constructor(width: Int, height: Int, buf: ByteBuffer) {
        texturePath = ""
        generateTexture(width, height, buf)
    }

    constructor(texturePath: String) {
        val stack = MemoryStack.stackPush()
        this.texturePath = texturePath
        val w: IntBuffer = stack.mallocInt(1)
        val h: IntBuffer = stack.mallocInt(1)
        val channels: IntBuffer = stack.mallocInt(1)

        val buf: ByteBuffer = STBImage.stbi_load(texturePath, w, h, channels, 4)
            ?: throw RuntimeException("Image file most likely not found: " + STBImage.stbi_failure_reason())

        val width = w.get()
        val height = h.get()

        generateTexture(width, height, buf)

        STBImage.stbi_image_free(buf)
    }

    fun bind() = glBindTexture(GL_TEXTURE_2D, textureId)
    fun cleanup() = glDeleteTextures(textureId)

    private fun generateTexture(width: Int, height: Int, buf: ByteBuffer) {
        textureId = glGenTextures()

        glBindTexture(GL_TEXTURE_2D, textureId)
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf)
        glGenerateMipmap(GL_TEXTURE_2D)
    }

    fun getTexturePath(): String = texturePath
}