package com.dothebestmayb.composestudy.side_effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Composable에서 특정 callback에 따라 register, unregister 해야 하는 경우 사용하기에 적합함
 * ex) Activity의 lifecycle에 따른 코드 실행, 특정 lifecycle에만 dynamic broadcast receiver 수신하기
 */
@Composable
fun DisposableEffectDemo(modifier: Modifier = Modifier) {
    // composable에 해당하는 lifecycle임
    val lifecycleOwner = LocalLifecycleOwner.current

    // lifecycle이 변경될 때마다 람다 블록이 실행됨
    DisposableEffect(lifecycleOwner.lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    println("OnStart was called!")
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // LaunchedEffect와의 차이점
        // composable이 composition을 leave하기 전에 호출된다.
        onDispose {
            println("Observer was disposed!")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
