package net.icxd.engine.util

import net.icxd.engine.graphics.Material
import net.icxd.engine.graphics.Mesh
import net.icxd.engine.graphics.Model
import net.icxd.engine.graphics.TextureCache
import org.joml.Vector4f
import org.lwjgl.PointerBuffer
import org.lwjgl.assimp.AIColor4D
import org.lwjgl.assimp.AIMaterial
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AIScene
import org.lwjgl.assimp.AIString
import org.lwjgl.assimp.AIVector3D
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.system.MemoryStack
import java.io.File
import java.nio.IntBuffer

object ModelLoader {
    @JvmStatic
    fun loadModel(modelId: String, modelPath: String, textureCache: TextureCache): Model {
        return loadModel(
            modelId, modelPath, textureCache, aiProcess_GenSmoothNormals or aiProcess_JoinIdenticalVertices or
                    aiProcess_Triangulate or aiProcess_FixInfacingNormals or aiProcess_CalcTangentSpace or aiProcess_LimitBoneWeights or
                    aiProcess_PreTransformVertices
        )
    }

    @JvmStatic
    fun loadModel(modelId: String, modelPath: String, textureCache: TextureCache, flags: Int): Model {
        val file = File(modelPath)
        val modelDir: String = file.parent ?: throw RuntimeException("Model path '$modelPath' doesn't exist.")
        val aiScene: AIScene = aiImportFile(modelPath, flags) ?: throw RuntimeException("Error loading model $modelPath")

        val numMaterials = aiScene.mNumMaterials()
        val materialList: MutableList<Material> = mutableListOf()
        for (i: Int in 0 until numMaterials) {
            val aiMaterial: AIMaterial = AIMaterial.create(aiScene.mMaterials()?.get(i) ?: throw RuntimeException("Failed to create AIMaterial"))
            materialList.add(processMaterial(aiMaterial, modelDir, textureCache))
        }

        val numMeshes = aiScene.mNumMeshes()
        val aiMeshes: PointerBuffer = aiScene.mMeshes() ?: throw RuntimeException("Cannot get meshes")
        val defaultMaterial = Material()
        for (i: Int in 0 until numMeshes) {
            val aiMesh: AIMesh = AIMesh.create(aiMeshes.get(i))
            val mesh: Mesh = processMesh(aiMesh)
            val materialIdx = aiMesh.mMaterialIndex()
            val material = if (materialIdx >= 0 && materialIdx < materialList.size) materialList[materialIdx] else defaultMaterial
            material.getMeshList().add(mesh)
        }

        if (defaultMaterial.getMeshList().isNotEmpty())
            materialList.add(defaultMaterial)

        return Model(modelId, materialList)
    }

    @JvmStatic
    private fun processIndices(aiMesh: AIMesh): IntArray {
        val indices: MutableList<Int> = mutableListOf()
        val numFaces = aiMesh.mNumFaces()
        val aiFaces = aiMesh.mFaces()
        for (i in 0 until numFaces) {
            val aiFace = aiFaces.get(i)
            val buffer = aiFace.mIndices()
            while (buffer.remaining() > 0) {
                indices.add(buffer.get())
            }
        }
        return indices.toIntArray()
    }

    @JvmStatic
    private fun processMaterial(aiMaterial: AIMaterial, modelDir: String, textureCache: TextureCache): Material {
        val material = Material()
        MemoryStack.stackPush().use { stack ->
            val color = AIColor4D.create()

            val result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color)

            if (result == aiReturn_SUCCESS)
                material.setDiffuseColor(Vector4f(color.r(), color.g(), color.b(), color.a()))

            val aiTexturePath: AIString = AIString.calloc(stack);
            aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, aiTexturePath, null as IntBuffer?,
                null, null, null, null, null)
            val texturePath: String = aiTexturePath.dataString()
            if (texturePath.isNotEmpty()) {
                material.setTexturePath(modelDir + File.separator + File(texturePath).name)
                textureCache.createTexture(material.getTexturePath())
                material.setDiffuseColor(Material.DEFAULT_COLOR)
            }

            return material;
        }
    }

    @JvmStatic
    private fun processMesh(aiMesh: AIMesh): Mesh {
        val vertices = processVertices(aiMesh)
        var textCoords = processTextCoords(aiMesh)
        val indices = processIndices(aiMesh)

        if (textCoords.isEmpty()) {
            val numElements = (vertices.size / 3) * 2
            textCoords = FloatArray(numElements)
        }

        return Mesh(vertices, textCoords, indices)
    }

    @JvmStatic
    private fun processTextCoords(aiMesh: AIMesh): FloatArray {
        val buffer: AIVector3D.Buffer = aiMesh.mTextureCoords(0) ?: return floatArrayOf()
        val data = FloatArray(buffer.remaining() * 2)
        var pos = 0
        while (buffer.remaining() > 0) {
            val textCoord = buffer.get()
            data[pos++] = textCoord.x()
            data[pos++] = 1 - textCoord.y()
        }
        return data
    }

    @JvmStatic
    private fun processVertices(aiMesh: AIMesh): FloatArray {
        val buffer: AIVector3D.Buffer = aiMesh.mVertices()
        val data = FloatArray(buffer.remaining() * 3)
        var pos = 0
        while (buffer.remaining() > 0) {
            val textCoord = buffer.get()
            data[pos++] = textCoord.x()
            data[pos++] = textCoord.y()
            data[pos++] = textCoord.z()
        }
        return data
    }
}