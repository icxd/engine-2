package net.icxd.engine.scene

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera {
    private val direction: Vector3f = Vector3f()
    private val position: Vector3f = Vector3f()
    private val right: Vector3f = Vector3f()
    private val up: Vector3f = Vector3f()
    private val rotation: Vector2f = Vector2f()
    private val viewMatrix: Matrix4f = Matrix4f()

    fun getPosition() = position
    fun getViewMatrix() = viewMatrix

    fun addRotation(x: Float, y: Float) { rotation.add(x, y); recalculate() }
    fun setPosition(x: Float, y: Float, z: Float) { position.set(x, y, z); recalculate() }
    fun setRotation(x: Float, y: Float) { rotation.set(x, y); recalculate() }

    fun moveForward(inc: Float) { viewMatrix.positiveZ(direction).negate().mul(inc); position.add(direction); recalculate() }
    fun moveBackward(inc: Float) { viewMatrix.positiveZ(direction).negate().mul(inc); position.sub(direction); recalculate() }
    fun moveRight(inc: Float) { viewMatrix.positiveX(right).mul(inc); position.add(right); recalculate() }
    fun moveLeft(inc: Float) { viewMatrix.positiveX(right).mul(inc); position.sub(right); recalculate() }
    fun moveUp(inc: Float) { viewMatrix.positiveY(up).mul(inc); position.add(up); recalculate() }
    fun moveDown(inc: Float) { viewMatrix.positiveY(up).mul(inc); position.sub(up); recalculate() }

    private fun recalculate() = viewMatrix.identity().rotateX(rotation.x).rotateY(rotation.y).translate(-position.x, -position.y, -position.z)
}