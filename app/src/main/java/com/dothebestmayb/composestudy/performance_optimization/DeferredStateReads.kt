package com.dothebestmayb.composestudy.performance_optimization

import android.R.attr.onClick
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.dothebestmayb.composestudy.R
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.launch

@Composable
fun DeferredStateReads(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val fabOffset by remember {
        derivedStateOf {
            val percentage = 1f - (state.firstVisibleItemIndex / 10f)
            (percentage * 100.dp).coerceIn(
                minimumValue = 0.dp,
                maximumValue = 100.dp,
            )
        }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        label = "color animation",
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse,
        )
    )

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            MovingFloatingActionButton(
                offset = { fabOffset },
                color = { color },
                onClick = {
                    scope.launch {
                        state.animateScrollToItem(0)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = state,
            modifier = modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

/**
 * Composable은 input 파라미터가 변경되면 recomposition 된다.
 * scroll할 때 변경된 offset 값이 전달되어 recomposition이 발생되고 있다.
 *
 * recomposition을 방지하기 위해 offset을 람다 블록 형식으로 전달해서, 필요한 곳에서 사용하도록 변경
 */
@Composable
fun MovingFloatingActionButton(
//    offset: Dp,
    offset: () -> Dp,
    color: () -> Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            // offset 파리미터에 변경되는 값을 전달하면 modifier가 계속 변경되면서 recomposition이 계속 발생함
//            .offset(y = offset),
            // offset 람다 블록은 layout phase에 처리되기 때문에 recomposition이 발생하지 않음
            .offset {
                IntOffset(
                    x = 0,
                    y = with(density) { offset().roundToPx() }
                )
            }
            // background는 color 값이 변경되면 recomposition 발생
            .background(color())
            // drawBehind 람다 블록은 layout phase에 처리되기 때문에 recomposition이 발생하지 않음
            .drawBehind {
                drawRect(
                    color = color(),
                )
            }
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_keyboard_arrow_up_24),
            contentDescription = "Scroll to Top"
        )
    }
}


@Preview
@Composable
fun DeferredStateReadsPreview(modifier: Modifier = Modifier) {
    ComposeStudyTheme {
        DeferredStateReads()
    }
}
