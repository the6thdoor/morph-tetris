package com.inferno

import com.morph.engine.core.GameApplication

fun main() {
    val game = TetrisGame(800, 600, 60f, false)
    val gameLauncher = GameApplication(game)
    gameLauncher.launchGame()
}
