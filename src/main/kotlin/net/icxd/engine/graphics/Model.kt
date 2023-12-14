package net.icxd.engine.graphics

import net.icxd.engine.scene.Entity

class Model(private val id: String, private val materialList: List<Material>) {
    private val entitiesList: MutableList<Entity> = mutableListOf()

    fun cleanup() = materialList.forEach(Material::cleanup)
    fun getEntitiesList() = entitiesList
    fun getId() = id
    fun getMaterialList(): List<Material> = materialList
}