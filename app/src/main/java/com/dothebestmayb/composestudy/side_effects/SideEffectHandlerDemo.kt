package com.dothebestmayb.composestudy.side_effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier

/**
 * recomposition이 성공한 이후 매번 호출된다.
 *
 * Composable이 아닌 람다 블록을 제공
 */
@Composable
fun SideEffectHandlerDemo(modifier: Modifier = Modifier) {
    SideEffect {

    }
}
