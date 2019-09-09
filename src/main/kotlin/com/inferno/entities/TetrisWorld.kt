package com.inferno.entities

import com.morph.engine.core.Game
import com.morph.engine.core.TileWorld
import com.inferno.pieces.Piece

/**
 * Created by Fernando on 2/11/2017.
 */
class TetrisWorld(game: Game, width: Int, height: Int, tileSize: Float) : TileWorld(game, width, height, tileSize) {
    override fun init() {}

    private val pieces: MutableList<Piece> = mutableListOf()

    fun clearAll() {
        pieces.clear()

        for (e in this.entities) {
            removeEntity(e)
        }
    }

    fun addPiece(p: Piece): Boolean {
        pieces.add(p)
        return addEntityGrid(p, p.x, p.y)
    }

    fun addPieceIfValid(p: Piece): Boolean {
        return if (areEmpty(*p.blockLocations)) {
            addPiece(p)
        } else false

    }

    fun removePiece(p: Piece): Boolean {
        pieces.remove(p)
        for (y in 0 until p.height) {
            for (x in 0 until p.width) {
                if (p[x, y] != null)
                    if (!removeEntity(x + p.x, y + p.y))
                        return false
            }
        }

        return true
    }

    fun moveIfValid(p: Piece, dx: Int, dy: Int): Boolean {
        removePiece(p)
        p.translate(dx, dy)
        if (this.areEmpty(*p.blockLocations)) {
            addPiece(p)
            return true
        }
        p.translate(-dx, -dy)
        addPiece(p)

        return false
    }

    fun moveIfValid(p: Piece, newP: Piece): Boolean {
        removePiece(p)
        if (this.areEmpty(*newP.blockLocations)) {
            addPiece(newP)
            return true
        }
        addPiece(p)

        return false
    }

    fun moveToBottom(p: Piece) {
        removePiece(p)
        while (areEmpty(*p.blockLocations)) {
            p.translate(0, 1)
        }
        p.translate(0, -1)
        addPiece(p)
    }

    fun checkForFilledRows(): List<Int> = (0 until height).filter { y -> (0 until width).all { x -> this[x, y] != null } }

    fun checkForEmptyRows(startRow: Int): List<Int> = (startRow until height).filter { y -> (0 until width).all { x -> this[x, y] == null } }

    fun clearRow(row: Int) {
        for (i in 0 until width) {
            removeEntity(i, row)
        }
    }

    fun fillEmptyRows(emptyRows: List<Int>) {
        var remaining = emptyRows

        while (remaining.isNotEmpty()) {
            for (row in emptyRows) {
                val newRow = row - 1
                for (y in newRow downTo 0) {
                    for (x in 0 until width) {
                        if (this[x, y] != null)
                            translateEntity(x, y, 0, 1)
                    }
                }
            }

            remaining = checkForEmptyRows(emptyRows[0])
        }
    }

}
