package com.ps.wearosstopwatch.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController

@Composable
fun NavGraph() {
    val navController = rememberSwipeDismissableNavController()

    val viewModel = viewModel<StopWatchViewModel>()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val stopWatchText by viewModel.stopWatchText.collectAsStateWithLifecycle()

    SwipeDismissableNavHost(
        navController = navController, startDestination = Screens.STOP_WATCH_SCREEN
    ) {
        composable(Screens.STOP_WATCH_SCREEN) {
            StopWatchScreen(modifier = Modifier.fillMaxSize(),
                state = timerState,
                text = stopWatchText,
                onToggleRunning = { viewModel.toggleRunning() },
                onReset = { viewModel.resetTimer() },
                onNavigateToTimer = { navController.navigate(Screens.TIMER_SCREEN) })
        }
        composable(Screens.TIMER_SCREEN) {
            TimerScreen(onNavigateToStopWatch = { navController.navigate(Screens.STOP_WATCH_SCREEN) })
        }
    }
}