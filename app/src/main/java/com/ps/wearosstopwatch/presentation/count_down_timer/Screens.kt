package com.ps.wearosstopwatch.presentation.count_down_timer


sealed class Screens(val route: String){

    companion object{
        const val STOP_WATCH_SCREEN = "stop_watch"
        const val TIMER_SCREEN = "timer"
    }
    object StopWatchScreen: Screens(route = STOP_WATCH_SCREEN)
    object TimerScreen: Screens(route = TIMER_SCREEN)
}
