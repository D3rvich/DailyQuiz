package ru.d3rvich.quiz.impl.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal const val TimerMaxValue: Long = 5 * 60 * 1000 // 5 minutes

internal class Timer(
    private val scope: CoroutineScope,
    private val maxValue: Long = TimerMaxValue,
    private val tick: Long = DefaultTick,
    private val onTick: (currentTimer: Long) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timerJob: Job? = null

    private var currentTimer: Long = 0

    fun start() {
        stop()
        resume()
    }

    fun pause() {
        timerJob?.cancel()
    }

    fun resume() {
        if (timerJob?.isActive == true) return

        timerJob = scope.launch {
            while (isActive && currentTimer < maxValue) {
                delay(tick)
                currentTimer += tick
                onTick(currentTimer)
            }
            if (currentTimer >= maxValue) {
                onFinish()
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        currentTimer = 0
    }
}

private const val DefaultTick: Long = 1000L