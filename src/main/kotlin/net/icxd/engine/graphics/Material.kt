package net.icxd.engine.graphics

import org.joml.Vector4f

class Material {
    companion object {
        val DEFAULT_COLOR: Vector4f = Vector4f(0.0f, 0.0f, 0.0f, 1.0f)
    }

    private var diffuseColor: Vector4f = DEFAULT_COLOR
    private val meshList: MutableList<Mesh> = mutableListOf()
    private var texturePath: String? = null

    fun cleanup() = meshList.forEach(Mesh::cleanup)

    fun getDiffuseColor(): Vector4f = diffuseColor
    fun getMeshList(): MutableList<Mesh> = meshList
    fun getTexturePath(): String = texturePath ?: TextureCache.DEFAULT_TEXTURE

    fun setDiffuseColor(diffuseColor: Vector4f) { this.diffuseColor = diffuseColor }
    fun setTexturePath(path: String) { texturePath = path }
}