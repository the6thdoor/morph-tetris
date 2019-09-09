package com.inferno

import com.inferno.gui.*
import com.morph.engine.core.OrthoCam2D
import com.morph.engine.math.Vector2f
import com.morph.engine.util.State
import com.morph.engine.util.StateMachine
import com.inferno.util.Stopwatch
import com.morph.engine.core.Game
import com.inferno.util.Timer
import com.inferno.entities.TetrisWorld
import com.inferno.pieces.Piece
import com.inferno.pieces.PieceFactory
import com.morph.engine.input.InputMapping
import com.morph.engine.input.KeyPress
import com.morph.engine.input.KeyRelease
import org.lwjgl.glfw.GLFW

/**
 * Created by Fernando on 2/10/2017.
 */
class TetrisGame(width: Int, height: Int, fps: Float, fullscreen: Boolean) : Game(width, height, "Inferno Tetris", fps, fullscreen) {
    private lateinit var nextPiece: Piece
    private lateinit var w: TetrisWorld

    private val stateMachine: StateMachine = StateMachine(State("Infinite Tetris"))

    private var gameLost = false
    private var restartGame = false

    private var restartState = "Infinite Tetris"

    private var bypassLock = false
    private var advance = false

    private var dropInterval = 1.0f // Time in seconds that a piece takes to drop one level
    private var regularDropInterval = 1.0f
    private val dropTimer: Timer = Timer(dropInterval, this::timerTick)

    private val lockLimit = 0.5f
    private val lockTimer: Stopwatch = Stopwatch(lockLimit, { }, this::lockTimer)

    private var score = 0

    private var currentGUI: TetrisGUI? = null
    private var lossGUI: LossGUI? = null

    override fun getWorld(): TetrisWorld {
        return world as TetrisWorld
    }

    override fun initGame() {
        this.setWorld(TetrisWorld(this, WIDTH, HEIGHT, TILE_SIZE))
        val ratio = width.toFloat() / height.toFloat()
        //        GLRenderingEngine.setProjectionMatrix(MatrixUtils.getOrthographicProjectionMatrix(WORLD_SIZE, 0, 0, WORLD_SIZE * ratio, -1, 1));
        w = getWorld()

        camera = OrthoCam2D(Vector2f(WORLD_SIZE.toFloat() * ratio * 0.5f, WORLD_SIZE * 0.5f), 0f, WORLD_SIZE.toFloat() * ratio, WORLD_SIZE.toFloat())
        w.setXOffset(8.33f)

        stateMachine.addPossibilities("Main Menu", "End Screen", "Loss", "Infinite Tetris")
        stateMachine.addTransition("*", "Loss", this::initLoss)
        stateMachine.addTransition("*", "Infinite Tetris", this::initInfiniteTetris)

        initInfiniteTetris()

        val inputMapping = InputMapping()

        inputMapping.mapKey(GLFW.GLFW_KEY_LEFT, KeyPress) {
            if (!gameLost) {
                w.moveIfValid(nextPiece, -1, 0)
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_RIGHT, KeyPress) {
            if (!gameLost) {
                w.moveIfValid(nextPiece, 1, 0)
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_UP, KeyPress) {
            if (!gameLost) {
                val newPiece = nextPiece.rotateRight()
                if (w.moveIfValid(nextPiece, newPiece))
                    nextPiece = newPiece
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_X, KeyPress) {
            if (!gameLost) {
                val newPiece = nextPiece.rotateRight()
                if (w.moveIfValid(nextPiece, newPiece))
                    nextPiece = newPiece
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_LEFT_CONTROL, KeyPress) {
            if (!gameLost) {
                val newPiece = nextPiece.rotateLeft()
                if (w.moveIfValid(nextPiece, newPiece))
                    nextPiece = newPiece
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_Z, KeyPress) {
            if (!gameLost) {
                val newPiece = nextPiece.rotateLeft()
                if (w.moveIfValid(nextPiece, newPiece))
                    nextPiece = newPiece
                if (lockTimer.isRunning && !lockTimer.isStopped)
                    lockTimer.restart()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_SPACE, KeyPress) {
            if (!gameLost) {
                w.moveToBottom(nextPiece)
                bypassLock = true
                timerTick()
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_DOWN, KeyPress) {
            if (!gameLost) {
                dropInterval = 0.05f
                dropTimer.setInterval(dropInterval)
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_DOWN, KeyRelease) {
            if (!gameLost) {
                dropInterval = regularDropInterval
                dropTimer.setInterval(dropInterval)
            }
        }

        inputMapping.mapKey(GLFW.GLFW_KEY_L, KeyRelease, this::onLoss)

        this.inputMapping = inputMapping
    }

    private fun lockTimer() {
        println("Lock timer ended.")
        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM)
        resolveFilledRows()

        if (!w.addPieceIfValid(nextPiece)) {
            onLoss()
        }

        dropTimer.restart()
    }

    private fun initLoss() {
        lossGUI = LossGUI(this, stateMachine.currentStateName)
        addGUI(lossGUI)
    }

    private fun initInfiniteTetris() {
        gameLost = false
        renderingEngine.setClearColor(0.1f, 0.1f, 0.1f, 0f)

        w.clearAll()

        nextPiece = PieceFactory.getPiece(PieceFactory.PieceType.RANDOM)
        w.addPiece(nextPiece)

        dropInterval = 1.0f
        regularDropInterval = dropInterval
        dropTimer.setInterval(dropInterval)
        lockTimer.setStoppedAction { this.lockTimer() }
        dropTimer.start()

        currentGUI?.let { removeGUI(currentGUI) }
        currentGUI = InfiniteTetrisGUI(this, width, height, WORLD_SIZE.toFloat())
        addGUI(currentGUI)
    }

    override fun preGameUpdate() {

    }

    override fun fixedGameUpdate(dt: Float) {
        dropTimer.step(dt)
        lockTimer.tick(dt)

        if (restartGame) {
            restartWithState(restartState)
        }
    }

    private fun restartWithState(state: String) {
        gameLost = false
        removeGUI(lossGUI)
        stateMachine.changeState(state)
        dropTimer.start()

        restartGame = false
    }

    override fun postGameUpdate() {}

    private fun timerTick() {
        if (!w.moveIfValid(nextPiece, 0, 1)) {
            if (!bypassLock && !advance) {
                dropTimer.stop()
                lockTimer.restart()
            } else {
                bypassLock = false
                advance = false
            }
        }
    }

    private fun onLoss() {
        println("You lose! Score: $score")
        gameLost = true
        stateMachine.changeState("Loss")
        dropTimer.stop()
    }

    fun signalRestart(state: String) {
        restartGame = true
        restartState = state
    }

    private fun resolveFilledRows() {
        val filledRows = w.checkForFilledRows()

        if (filledRows.isNotEmpty()) {
            for (row in filledRows) {
                w.clearRow(row)
            }

            val points = 50 * filledRows.size + (filledRows.size - 1) * 10
            score += points
            println("Scored $points points! New Score: $score")
            currentGUI!!.updateScore(points, filledRows.size)

            w.fillEmptyRows(filledRows)
        }
    }

    override fun handleInput() { }

    companion object {
        private const val WIDTH = 10
        private const val HEIGHT = 20
        private const val TILE_SIZE = 1f
        private const val WORLD_SIZE = 20
    }
}
