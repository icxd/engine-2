package net.icxd.engine.graphics

import net.icxd.engine.scene.Entity

class Model(private val id: String, private val meshList: List<Mesh>) {
    private val entitiesList: MutableList<Entity> = mutableListOf()

    fun cleanup() = meshList.forEach { it.cleanup() }
    fun getEntitiesList() = entitiesList
    fun getId() = id
    fun getMeshList() = meshList
}