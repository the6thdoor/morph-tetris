package com.inferno.gui

import com.morph.engine.core.Game
import com.morph.engine.graphics.Color
import com.morph.engine.math.Vector2f
import com.morph.engine.newgui.TextElement

/**
 * Created by Fernando on 3/18/2017.
 */
class InfiniteTetrisGUI(game: Game, width: Int, height: Int, worldSize: Float) : TetrisGUI(game, width, height, worldSize, -1) {
    override fun init() {}

    override fun load() {
        super.load()

        disableGoalTracking()
        addElement(TextElement("infinite-tetris-mode-name", "Infinite Tetris", "fonts/Roboto Mono/RobotoMono-Regular.ttf", 24, Vector2f(570f, 60f), Color(0f, 0.75f, 0f), -20))
    }
}
