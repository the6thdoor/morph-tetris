package com.inferno.pieces

import com.morph.engine.entities.EntityGrid
import com.morph.engine.math.Vector2f

import java.util.ArrayList

/**
 * Created by Fernando on 2/11/2017.
 */
class Piece(width: Int, height: Int) : EntityGrid(width, height) {
    var x: Int = 0
    var y: Int = 0

    /**
     * WARNING: X and Y will be truncated!
     */
    var position: Vector2f
        get() = Vector2f(x.toFloat(), y.toFloat())
        set(position) {
            this.x = position.x.toInt()
            this.y = position.y.toInt()
        }

    val blockLocations: Array<Pair<Int, Int>>
        get() = getBlockLocationsWithOffset(0, 0)

    fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun translate(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    fun getBlockLocationsWithOffset(dx: Int, dy: Int): Array<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (this[x, y] != null)
                    result.add(Pair(this.x + x + dx, this.y + y + dy))
            }
        }

        return result.toTypedArray()
    }

    fun rotateLeft(): Piece {
        val newPiece = Piece(height, width)
        for (y in 0 until height) {
            for (x in 0 until width) {
                newPiece[y, width - x - 1] = this[x, y]
            }
        }

        newPiece.setPosition(x, y)
        return newPiece
    }

    fun rotateRight(): Piece {
        val newPiece = Piece(height, width)
        for (y in 0 until height) {
            for (x in 0 until width) {
                newPiece[height - y - 1, x] = this[x, y]
            }
        }

        newPiece.setPosition(x, y)
        return newPiece
    }
}
