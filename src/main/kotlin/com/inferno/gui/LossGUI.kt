package com.inferno.gui

import com.inferno.TetrisGame
import com.inferno.util.Stopwatch
import com.inferno.util.TextUtils
import com.morph.engine.core.Game
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.math.Vector2f
import com.morph.engine.newgui.Button
import com.morph.engine.newgui.GUI
import com.morph.engine.newgui.Panel
import com.morph.engine.newgui.TextElement
import com.morph.engine.physics.components.Transform2D

/**
 * Created by Fernando on 3/8/2017.
 */
class LossGUI(game: Game, private val lastCircle: String) : GUI(game) {
    private var lossText: TextElement? = null
    private var lossPanel: Panel? = null

    private var retryButton: Button? = null
    private var exitButton: Button? = null

    private var lossTimer: Stopwatch? = null
    private var lossButtonTimer: Stopwatch? = null

    override fun load() {
        lossPanel = Panel("Loss Panel", Vector2f(0f, 0f), Vector2f(800f, 600f), Color(0f, 0f, 0f, 0f), Texture("textures/solid.png"))
        lossPanel!!.depth = -50

        addElement(lossPanel!!)

        lossText = TextUtils.centerWithin("lossText", "You lost.", "fonts/Roboto Mono/RobotoMono-Bold.ttf", 64, Color(0f, 0f, 0f, 0f), lossPanel!!)
        addElement(lossText!!)

        lossTimer = Stopwatch(4.0f, { }, {
            exitButton = Button("exitButton",
                    "Exit",
                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                    24,
                    Color(0f, 0f, 0f),
                    Color(0.5f, 0f, 0f, 0f),
                    Texture("textures/solid.png"),
                    Texture("textures/solid.png"),
                    Transform2D(position = Vector2f(300f, 145f), scale = Vector2f(180f, 50f)),
                    -51)

            retryButton = Button("retryButton",
                    "Retry",
                    "fonts/Roboto Mono/RobotoMono-Regular.ttf",
                    24,
                    Color(0f, 0f, 0f),
                    Color(0f, 0.5f, 0f, 0f),
                    Texture("textures/solid.png"),
                    Texture("textures/solid.png"),
                    Transform2D(position = Vector2f(500f, 145f), scale = Vector2f(180f, 50f)),
                    -51)

            retryButton!!.onClick { (game as TetrisGame).signalRestart(lastCircle) }

            exitButton!!.onClick { game.stop() }

            addElement(retryButton!!)
            addElement(exitButton!!)

            lossButtonTimer = Stopwatch(2.0f, { }, { })
            lossButtonTimer!!.setTickAction { time, timeLimit ->
                val alpha = time / timeLimit

                val exitButtonColor = Color(0.5f, 0f, 0f, alpha)
                val retryButtonColor = Color(0f, 0.5f, 0f, alpha)

                retryButton!!.renderData.tint = retryButtonColor
                exitButton!!.renderData.tint = exitButtonColor
            }
            lossButtonTimer!!.start()
        })

        lossTimer!!.setTickAction { time, timeLimit ->
            val alpha = time / timeLimit

            val panelColor = Color(0f, 0f, 0f, alpha * 0.8f)
            val textColor = Color(0.5f, 0f, 0f, alpha)

            lossText!!.renderData.tint = textColor
            lossPanel!!.renderData.tint = panelColor
        }

        lossTimer!!.start()
    }

    override fun unload() {}

    override fun fixedUpdate(dt: Float) {
        if (lossTimer != null) {
            lossTimer!!.tick(dt)
        }

        if (lossButtonTimer != null) {
            lossButtonTimer!!.tick(dt)
        }
    }

    override fun init() {}
}
