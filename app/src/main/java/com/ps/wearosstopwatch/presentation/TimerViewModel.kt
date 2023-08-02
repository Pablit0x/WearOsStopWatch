package com.ps.wearosstopwatch.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModel : ViewModel() {

    private val _remainingTime = MutableStateFlow(60L * 1000)

    private val _timerState = MutableStateFlow(TimerState.RESET)
    val timerState = _timerState.asStateFlow()

    val countdownText = _remainingTime.map { millis ->
        (millis / 1000).toString()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "60"
    )


    init {
        _timerState.flatMapLatest { timerState ->
            getTimerFlow(isRunning = timerState == TimerState.RUNNING)
        }.onEach { timeDiff ->
            _remainingTime.update { it - timeDiff }
        }.launchIn(viewModelScope) // Add this line to start collecting data from the flow.
    }

    fun toggleRunning() {
        when (timerState.value) {
            TimerState.RUNNING -> _timerState.update { TimerState.PAUSED }
            TimerState.PAUSED,
            TimerState.RESET -> _timerState.update { TimerState.RUNNING }
        }
    }

    fun resetTimer() {
        _timerState.update { TimerState.RESET }
        _remainingTime.update { 60L * 1000 }
    }

    fun increaseTimer() {
        _remainingTime.update { it + (10L * 100) }
    }

    fun decreaseTimer() {
        _remainingTime.update { it - (10L * 100) }
    }

    private fun getTimerFlow(isRunning: Boolean): Flow<Long> {
        return flow {
            var startTime = System.currentTimeMillis()
            while (isRunning && _remainingTime.value > 0) {
                val currentTime = System.currentTimeMillis()
                val timeDiff = currentTime - startTime
                emit(timeDiff)
                startTime = currentTime
                delay(10L)
            }
        }
    }
}
