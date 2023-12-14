package net.icxd.engine.scene

import net.icxd.engine.graphics.Model
import net.icxd.engine.graphics.TextureCache

class Scene(private val width: Int, private val height: Int) {
    private val modelMap: MutableMap<String, Model> = mutableMapOf()
    private val projection: Projection = Projection(width, height)
    private val textureCache: TextureCache = TextureCache()
    private val camera: Camera = Camera()

    fun addEntity(entity: Entity) {
        val modelId = entity.getModelId()
        val model = modelMap[modelId]
        model?.getEntitiesList()?.add(entity)
    }

    fun addModel(model: Model) { modelMap[model.getId()] = model }
    fun cleanup() = modelMap.values.forEach(Model::cleanup)

    fun getModelMap() = modelMap
    fun getProjection() = projection
    fun getTextureCache(): TextureCache = textureCache
    fun getCamera(): Camera = camera;
    fun resize(width: Int, height: Int) = projection.updateProjMatrix(width, height)
}