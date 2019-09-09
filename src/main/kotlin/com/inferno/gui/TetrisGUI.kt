package com.inferno.gui

import com.inferno.TetrisGame
import com.inferno.util.BlendingUtils
import com.inferno.util.Stopwatch
import com.morph.engine.core.Game
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.math.Vector2f
import com.morph.engine.newgui.*
import com.morph.engine.physics.components.Transform2D

/**
 * Created by Fernando on 3/6/2017.
 */
open class TetrisGUI(game: Game, protected var width: Int, protected var height: Int, private val worldSize: Float, private val goal: Int) : GUI(game) {
    private var score: Int = 0

    private var scoreText: TextField? = null
    private var scoreIncrementText: TextField? = null
    private var goalText: TextElement? = null
    private var goalAmountText: TextField? = null

    private var pointTimer: Stopwatch? = null

    init {
        this.score = 0
    }

    override fun load() {
        val panelWidth = 12.5f * worldSize

        addElement(Panel("panel1", Vector2f(0f, 0f), Vector2f(panelWidth, height.toFloat()), Color(0.05f, 0.05f, 0.05f), Texture("textures/solid.png")))
        addElement(Panel("panel2", Vector2f(width - panelWidth, 0f), Vector2f(panelWidth, height.toFloat()), Color(0.05f, 0.05f, 0.05f), Texture("textures/solid.png")))

        addElement(TextElement("textLn1", "TETRIS", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, Vector2f(160f, 540f), Color(1f, 1f, 1f), -20))
        addElement(TextElement("textLn2", "from HELL", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, Vector2f(130f, 505f), Color(0.5f, 0.05f, 0.05f), -20))

        addElement(TextElement("authorName", "Fernando Gonzalez", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24,  Vector2f(39f, 60f), Color(1f, 1f, 1f),-20))

        addElement(TextElement("scoreFixedText", "Score", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, Vector2f(570f, 520f), Color(1f, 1f, 1f), -20))
        scoreText = TextField("scoreNumberText", "0", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, Color(0f, 1f, 0f), Vector2f(570f, 480f), -20)

        addElement(scoreText!!)

        goalText = TextElement("goalText", "Goal", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 64, Vector2f(570f, 400f), Color(1f, 1f, 1f), -20)
        addElement(goalText!!)

        goalAmountText = TextField("goalAmountText", goal.toString(), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, Color(0f, 1f, 0f), Vector2f(570f, 360f), -20)
        addElement(goalAmountText!!)

        scoreIncrementText = TextField("scoreIncrementText", "", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 28, Color(0f, 0.4f, 0f), Vector2f(575f, 450f), -20)
        addElement(scoreIncrementText!!)
    }

    override fun unload() {

    }

    fun resetScore() {
        this.score = 0
        scoreText?.replaceText("$score")
//        removeElement(scoreText!!)
//        scoreText = TextElement("scoreText", score.toString(), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, Vector2f(570f, 480f), Color(0f, 1f, 0f),-20)
//        addElement(scoreText!!)
    }

    fun updateScore(points: Int, lines: Int) {
        this.score += points
        scoreText?.replaceText("$score")
//        removeElement(scoreText!!)
//        scoreText = TextElement("scoreText", score.toString(), "fonts/Roboto Mono/RobotoMono-Regular.ttf", 32, Vector2f(570f, 480f), Color(0f, 1f, 0f), -20)
//        addElement(scoreText!!)

        val scoreColor = Color(0f, points / 230f * 0.5f + 0.5f, 0f)

        val msg = when (lines) {
            1 -> "Single"
            2 -> "Double"
            3 -> "Triple"
            4 -> "Tetris"
            else -> ""
        }

//        scoreIncrementText = TextElement("scoreIncrementText", "+$points [$msg]", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 28, Vector2f(575f, 450f), Color(0f, 0.4f, 0f), -20)
//        addElement(scoreIncrementText!!)
        scoreIncrementText?.replaceText("+$points [$msg]")

        pointTimer = Stopwatch(1.25f, stoppedAction = { scoreIncrementText!!.renderData.tint = Color(0.05f, 0.05f, 0.05f)})
        pointTimer!!.setTickAction { time, timeLimit ->
            val finish = Color(0.05f, 0.05f, 0.05f)
            val alpha = BlendingUtils.easeInQuartic(time / timeLimit)
            println(alpha)

            val currentColor = (scoreColor * alpha) + (finish * (1 - alpha))

            scoreIncrementText!!.renderData.tint = currentColor
        }
        pointTimer!!.start()
    }

    override fun fixedUpdate(dt: Float) {
        if (pointTimer != null) {
            pointTimer!!.tick(dt)
        }
    }

    fun disableGoalTracking() {
        removeElement(goalText!!)
        removeElement(goalAmountText!!)
    }

    override fun init() {

    }
}
