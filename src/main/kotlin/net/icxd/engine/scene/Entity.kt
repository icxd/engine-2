package net.icxd.engine.scene

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Entity(private val id: String, private val modelId: String) {
    private var modelMatrix: Matrix4f = Matrix4f()
    private var position: Vector3f = Vector3f()
    private var rotation: Quaternionf = Quaternionf()
    private var scale: Float = 1f

    fun getId() = id
    fun getModelId() = modelId
    fun getModelMatrix() = modelMatrix
    fun getPosition() = position
    fun getRotation() = rotation
    fun getScale() = scale

    fun setPosition(x: Float, y: Float, z: Float) { position = Vector3f(x, y, z) }
    fun setRotation(x: Float, y: Float, z: Float, angle: Float) { rotation.fromAxisAngleRad(x, y, z, angle) }
    fun setScale(scale: Float) { this.scale = scale }

    fun updateModelMatrix() { modelMatrix.translationRotateScale(position, rotation, scale) }
}