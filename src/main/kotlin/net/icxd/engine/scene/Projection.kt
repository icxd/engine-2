package net.icxd.engine.scene

import org.joml.Matrix4f

class Projection(private val width: Int, private val height: Int) {
    companion object {
        private val FOV: Float = Math.toRadians(60.0).toFloat()
        private const val Z_FAR: Float = 1000.0f
        private const val Z_NEAR: Float = 0.01f
    }

    private val projectionMatrix: Matrix4f = Matrix4f()

    init { updateProjMatrix(width, height) }

    fun getProjMatrix(): Matrix4f = projectionMatrix

    fun updateProjMatrix(width: Int, height: Int) {
        projectionMatrix.perspective(FOV, width.toFloat() / height.toFloat(), Z_NEAR, Z_FAR)
    }
}