package com.ps.wearosstopwatch.presentation.count_down_timer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.ps.wearosstopwatch.presentation.stop_watch.StopWatchScreen
import com.ps.wearosstopwatch.presentation.stop_watch.StopWatchViewModel

@Composable
fun NavGraph() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController, startDestination = Screens.STOP_WATCH_SCREEN
    ) {
        composable(Screens.STOP_WATCH_SCREEN) {

            val viewModel = viewModel<StopWatchViewModel>()
            val timerState by viewModel.timerState.collectAsStateWithLifecycle()
            val stopWatchText by viewModel.stopWatchText.collectAsStateWithLifecycle()

            StopWatchScreen(modifier = Modifier.fillMaxSize(),
                state = timerState,
                text = stopWatchText,
                onToggleRunning = { viewModel.toggleRunning() },
                onReset = { viewModel.resetStopWatch() },
                onNavigateToTimer = { navController.navigate(Screens.TIMER_SCREEN) })
        }
        composable(Screens.TIMER_SCREEN) {

            val viewModel = viewModel<TimerViewModel>()
            val timerState by viewModel.timerState.collectAsStateWithLifecycle()
            val countdownText by viewModel.countdownText.collectAsStateWithLifecycle()

            TimerScreen(state = timerState,
                countdownText = countdownText,
                onReset = viewModel::resetTimer,
                onToggleRunning = viewModel::toggleRunning,
                onNavigateToStopWatch = {
                    navController.navigate(Screens.STOP_WATCH_SCREEN)
                },
                onIncreaseTime = viewModel::increaseTimer,
                onDecreaseTime = viewModel::decreaseTimer)
        }
    }
}