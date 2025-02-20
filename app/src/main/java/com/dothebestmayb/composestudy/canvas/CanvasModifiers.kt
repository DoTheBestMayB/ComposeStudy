package com.dothebestmayb.composestudy.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Canvas는 DrawScope을 가진 Spacer다.
 *
 * ```kotlin
 * Spacer(modifier.drawBehind(onDraw))
 * ```
 *
 * 그래서 drawBehind를 modifier로 추가하면 다른 Layout도 draw가 가능하다.
 *
 * 즉, Canvas는 draw 할 수 있는 영역을 제공할 뿐이다.
 *
 * drawBehind는 Content보다 뒤에 그려진다. (modifier의 순서에 따라 그려지는 것은 동일)
 *
 * drawScope은 Density를 상속하기 때문에 dp.roundToPx과 같은 함수를 사용할 수 있다.
 */
@Composable
fun CanvasModifiersDemo(modifier: Modifier = Modifier) {
//    Canvas(
//        modifier = modifier
//            .fillMaxSize(),
//    ) {
//        drawCircle(color = Color.Red)
//    }

    // modifier를 이용해 Box를 draw 가능하도록 변경하기
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green) // draw를 다른 modifier와 조합해서 사용가능
            .drawBehind {
                // transform을 이용해 cliping 할 수 있다.
                withTransform(
                    transformBlock = {
                        rotate(
                            degrees = 90f // drawBlock을 90도 회전시킴
                        )
                    },
                    drawBlock = {
                        drawCircle(color = Color.Red)
                        drawLine(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Yellow,
                                    Color.Red
                                )
                            ),
                            start = Offset.Zero,
                            end = center,
                            strokeWidth = 10.dp.toPx(),
                        )
                    }
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello world!") // draw보다 텍스트가 위에 그려진다.
    }
}

/**
 *  canvas를 content 뒤에 그리는 것이 아니라
 *  content를 어디에 그릴지 결정할 수 있다.
 */
@Composable
private fun DrawWithContext(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green)
            .drawWithContent {
                drawCircle(Color.Red)
                drawContent()
                drawCircle(
                    color = Color.Yellow,
                    radius = 10.dp.toPx(),
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello world!") // draw보다 텍스트가 위에 그려진다.
    }
}

/**
 * onDrawWithContent 또는 onDrawBehind를 호출해서 canvas를 그릴 수 있다.
 * 차이점은 Caching을 제공한다.
 *
 * canvas에서 사용하는 state에 변화가 생길 때마다 invalidate 되고, 다시 그려진다.
 * 다시 그려질 때마다 매번 다시 계산하지 않길 바라는 상수와 같은 값이 있을 수 있다.
 *
 * 이러한 값을 CacheDrawScope에 선언하면 된다. state가 변경돼도 이 scope에 있는 값은 다시 계산하지 않고 재활용한다.
 *
 * 하지만 Canvas 크기가 변경되면 이 값도 다시 계산된다.
 */
@Composable
private fun DrawWithCache(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green)
            .drawWithCache {
                val complexLogicResult = (1..10_000).map {
                    it * it
                }
                onDrawWithContent {
                    drawCircle(Color.Red)
                    drawContent()
                    drawCircle(
                        color = Color.Yellow,
                        radius = 10.dp.toPx(),
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello world!") // draw보다 텍스트가 위에 그려진다.
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun CanvasModifiersDemoPreview() {
    ComposeStudyTheme {
        CanvasModifiersDemo()
    }
}

@Preview
@Composable
private fun DrawWithContextPreview() {
    ComposeStudyTheme {
        DrawWithContext()
    }
}

@Preview
@Composable
private fun DrawWithCachePreview() {
    ComposeStudyTheme {
        DrawWithCache()
    }
}
