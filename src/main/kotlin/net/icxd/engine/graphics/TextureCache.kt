package net.icxd.engine.graphics

class TextureCache {
    companion object {
        const val DEFAULT_TEXTURE: String = "resources/models/default/default_texture.png"
    }

    private val textureMap: MutableMap<String, Texture> = mutableMapOf()

    init { textureMap[DEFAULT_TEXTURE] = Texture(DEFAULT_TEXTURE) }

    fun cleanup() = textureMap.values.forEach(Texture::cleanup)
    fun createTexture(path: String): Texture = textureMap.computeIfAbsent(path, ::Texture)

    fun getTexture(path: String): Texture =
        if (textureMap[path] == null) textureMap[DEFAULT_TEXTURE]!! else textureMap[path]!!
}