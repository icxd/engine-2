package net.icxd.engine.graphics

import net.icxd.engine.scene.Scene
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*

class SceneRenderer {
    private val shaderProgram: ShaderProgram
    private lateinit var uniformsMap: UniformsMap

    init {
        val shaderModuleDataList = listOf(
            ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
            ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
        )
        shaderProgram = ShaderProgram(shaderModuleDataList)
        createUniforms()
    }

    fun cleanup() = shaderProgram.cleanup()

    private fun createUniforms() {
        uniformsMap = UniformsMap(shaderProgram.getProgramId())
        uniformsMap.createUniform("projectionMatrix")
        uniformsMap.createUniform("modelMatrix")
    }

    fun render(scene: Scene) {
        shaderProgram.bind()
        uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix())

        val models = scene.getModelMap().values
        for (model in models) {
            model.getMeshList().forEach { mesh ->
                glBindVertexArray(mesh.getVaoId())
                val entities = model.getEntitiesList()
                for (entity in entities) {
                    uniformsMap.setUniform("modelMatrix", entity.getModelMatrix())
                    glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0)
                }
            }
        }

        glBindVertexArray(0)
        shaderProgram.unbind()
    }
}