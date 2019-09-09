package com.inferno.util

import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Created by Fernando on 3/5/2017.
 */
class Stopwatch(val timeLimit: Float, private val interruptedAction: () -> Unit = {}, private var stoppedAction: () -> Unit = {}) {
    var isRunning: Boolean = false
        private set
    var isInterrupted: Boolean = false
        private set
    var isStopped: Boolean = false
        private set
    var time: Float = 0.toFloat()
        private set
    private var tickAction: (Float, Float) -> Unit = { _, _ -> }

    init {
        this.tickAction = { _, _ -> }
        isRunning = false
        isStopped = false
        isInterrupted = false
    }

    fun start() {
        isRunning = true
    }

    fun tick(dt: Float) {
        if (isRunning && !isStopped && !isInterrupted) {
            if (time >= timeLimit) {
                stoppedAction()
                isStopped = true
                isRunning = false
            }

            tickAction(time, timeLimit)
            time += dt
        }
    }

    fun interrupt() {
        interruptedAction()
        isInterrupted = true
    }

    fun restart() {
        isRunning = true
        isStopped = false
        isInterrupted = false
        time = 0f
    }

    fun endTimer() {
        isRunning = false
        isStopped = true
        isInterrupted = false
    }

    fun setTickAction(tick: (Float, Float) -> Unit) {
        this.tickAction = tick
    }

    fun setStoppedAction(stoppedAction: () -> Unit) {
        this.stoppedAction = stoppedAction
    }
}
