package com.inferno.pieces

import com.morph.engine.entities.Entity
import com.morph.engine.entities.EntityGrid
import com.morph.engine.graphics.Color
import com.inferno.entities.TetrisEntityFactory

/**
 * Created by Fernando on 2/10/2017.
 */
object PieceFactory {
    private var count: Int = 0

    private val i: Piece
        get() {
            val piece = Piece(4, 4)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("I-$count$i", Color(0f, 1f, 1f), 1f)
            }

            piece[0, 1] = blocks[0]
            piece[1, 1] = blocks[1]
            piece[2, 1] = blocks[2]
            piece[3, 1] = blocks[3]
            piece.y = -1

            count += 4

            return piece
        }

    private val o: Piece
        get() {
            val piece = Piece(2, 2)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("O-$count$i", Color(1f, 1f, 0f), 1f)
            }

            piece[0, 0] = blocks[0]
            piece[1, 0] = blocks[1]
            piece[0, 1] = blocks[2]
            piece[1, 1] = blocks[3]

            count += 4

            return piece
        }

    private val t: Piece
        get() {
            val piece = Piece(3, 3)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("T-$count$i", Color(1f, 0f, 1f), 1f)
            }

            piece[1, 0] = blocks[0]
            piece[0, 1] = blocks[1]
            piece[1, 1] = blocks[2]
            piece[2, 1] = blocks[3]

            count += 4

            return piece
        }

    private val j: Piece
        get() {
            val piece = Piece(3, 3)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("J-$count$i", Color(0f, 0f, 1f), 1f)
            }

            piece[0, 0] = blocks[0]
            piece[0, 1] = blocks[1]
            piece[1, 1] = blocks[2]
            piece[2, 1] = blocks[3]

            count += 4

            return piece
        }

    private val l: Piece
        get() {
            val piece = Piece(3, 3)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("L-$count$i", Color(1f, 0.5f, 0f), 1f)
            }

            piece[2, 0] = blocks[0]
            piece[0, 1] = blocks[1]
            piece[1, 1] = blocks[2]
            piece[2, 1] = blocks[3]

            count += 4

            return piece
        }

    private val s: Piece
        get() {
            val piece = Piece(3, 3)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("S-$count$i", Color(0f, 1f, 0f), 1f)
            }

            piece[1, 0] = blocks[0]
            piece[2, 0] = blocks[1]
            piece[0, 1] = blocks[2]
            piece[1, 1] = blocks[3]

            count += 4

            return piece
        }

    private val z: Piece
        get() {
            val piece = Piece(3, 3)
            val blocks = arrayOfNulls<Entity>(4)
            for (i in 0..3) {
                blocks[i] = TetrisEntityFactory.getBlock("Z-$count$i", Color(1f, 0f, 0f), 1f)
            }

            piece[0, 0] = blocks[0]
            piece[1, 0] = blocks[1]
            piece[1, 1] = blocks[2]
            piece[2, 1] = blocks[3]

            count += 4

            return piece
        }

    enum class PieceType {
        I, O, T, J, L, S, Z, RANDOM
    }

    fun getPiece(type: PieceType): Piece {
        return when (type) {
            PieceFactory.PieceType.I -> i
            PieceFactory.PieceType.O -> o
            PieceFactory.PieceType.T -> t
            PieceFactory.PieceType.J -> j
            PieceFactory.PieceType.L -> l
            PieceFactory.PieceType.S -> s
            PieceFactory.PieceType.Z -> z
            PieceFactory.PieceType.RANDOM -> {
                val rand = (Math.random() * 7).toInt()
                getPiece(rand)
            }
        }
    }

    fun getPiece(index: Int): Piece {
        return when (index) {
            0 -> i
            1 -> o
            2 -> t
            3 -> j
            4 -> l
            5 -> s
            6 -> z
            else -> throw RuntimeException("impossible")
        }
    }
}
