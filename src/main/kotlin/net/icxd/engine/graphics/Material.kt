package net.icxd.engine.graphics

class Material {
    private val meshList: MutableList<Mesh> = mutableListOf()
    private lateinit var texturePath: String

    fun cleanup() = meshList.forEach(Mesh::cleanup)

    fun getMeshList(): MutableList<Mesh> = meshList
    fun getTexturePath(): String = texturePath

    fun setTexturePath(path: String) { texturePath = path }
}