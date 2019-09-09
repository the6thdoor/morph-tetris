package com.inferno.graphics.shaders

import com.morph.engine.core.Camera
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.shaders.Uniforms
import com.morph.engine.graphics.components.RenderData
import com.morph.engine.graphics.components.light.Light
import com.morph.engine.math.Matrix4f
import com.morph.engine.physics.components.Transform

class TetrisShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
        addUniform("diffuseColor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, camera: Camera, screen: Matrix4f, lights: List<Light>) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", camera.modelViewProjection * mvp)
        setUniform1i("diffuse", 0)
        setUniform3f("diffuseColor", data.tint)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}
