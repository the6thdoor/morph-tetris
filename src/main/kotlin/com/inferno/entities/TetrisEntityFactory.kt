package com.inferno.entities

import com.morph.engine.entities.Entity
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.components.RenderData
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D
import com.inferno.graphics.shaders.TetrisShader

/**
 * Created by Fernando on 2/11/2017.
 */
object TetrisEntityFactory {
    fun getBlock(name: String, c: Color, size: Float): Entity {
        val block = EntityFactory.getCustomTintRectangle(name, size, size, c, TetrisShader())
        block.getComponent(RenderData::class.java)!!.setTexture(Texture("textures/tetrisBlockSV.png"), 0)
        return block
    }

    fun getBlockAt(name: String, position: Vector2f, c: Color, size: Float): Entity {
        val block = getBlock(name, c, size)
        block.getComponent(Transform2D::class.java)!!.position = position
        return block
    }
}
