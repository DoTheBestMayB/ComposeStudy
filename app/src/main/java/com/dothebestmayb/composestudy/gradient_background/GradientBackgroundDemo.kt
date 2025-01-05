package com.dothebestmayb.composestudy.gradient_background

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun GradientBackgroundDemo(
    modifier: Modifier = Modifier,
    startColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawWithCache {
                    onDrawBehind {
                        val endColor = Color.Transparent

                        // 1) 그라디언트 중심: Box 하단 중앙
                        val center = Offset(size.width / 2f, size.height)

                        // 2) 뷰포트의 최대 치수보다 크게 설정 (상단까지 부드럽게 퍼지도록)
                        val maxDim = maxOf(size.width, size.height) * 0.4f

                        // 3) '원형' 그라디언트 브러시 (실제 사용 시엔 translate/scale로 타원화)
                        val radialBrush = Brush.radialGradient(
                            colors = listOf(startColor, endColor),
                            center = Offset.Zero, // 실제 draw 때 옮길 예정
                            radius = maxDim
                        )

                        // 4) 그리기 시작
                        drawContext.canvas.save()

                        // 하단 가운데로 이동
                        drawContext.canvas.translate(center.x, center.y)

                        // 필요하다면 scale로 타원화(가로/세로 비율 조정)
                        val scaleX = 1.0f
                        val scaleY = 0.7f
                        drawContext.canvas.scale(scaleX, scaleY)

                        // 5) 대형 사각형(원 중심 기준) 영역에 그라디언트 그리기
                        drawRect(
                            brush = radialBrush,
                            topLeft = Offset(-maxDim, -maxDim),
                            size = Size(2 * maxDim, 2 * maxDim)
                        )

                        drawContext.canvas.restore()
                    }
                }
        )

        // 최종적으로 이 안에 들어갈 실제 content
        Box {
            content()
        }
    }
}

@Composable
fun GradientBackgroundOvalDemo(
    modifier: Modifier = Modifier,
    endPercent: Float = 0.05f,
    startColor: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawWithCache {
                    onDrawBehind {
                        val endColor = Color.Transparent

                        // 뷰포트의 최대 치수보다 크게 설정 (상단까지 부드럽게 퍼지도록)
                        val radius = maxOf(size.width, size.height) * 0.4f

                        // scale로 타원화(가로/세로 비율 조정)
                        val scaleX = 1.55f // 가로는 많이 차지하도록 설정
                        val scaleY = 0.7f

                        // 그라디언트 중심: Box 하단 중앙
                        val center = Offset(
                            x = size.width / 2f,
                            y = size.height * endPercent + radius * scaleY
                        )

                        // '원형' 그라디언트 브러시 (실제 사용 시엔 translate/scale로 타원화)
                        val radialBrush = Brush.radialGradient(
                            colors = listOf(startColor, endColor),
                            center = Offset.Zero, // 실제 draw 때 옮길 예정
                            radius = radius
                        )

                        // 그리기 시작
                        drawContext.canvas.save()

                        // 하단 가운데로 이동
                        drawContext.canvas.translate(center.x, center.y)


                        drawContext.canvas.scale(scaleX, scaleY)

                        // 대형 사각형(원 중심 기준) 영역에 그라디언트 그리기
                        drawRect(
                            brush = radialBrush,
                            topLeft = Offset(-radius, -radius),
                            size = Size(2 * radius, 2 * radius)
                        )

                        drawContext.canvas.restore()
                    }
                }
        )

        // 최종적으로 이 안에 들어갈 실제 content
        Box {
            content()
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun GradientBackgroundDemoPreview() {
    ComposeStudyTheme {
        GradientBackgroundOvalDemo(
            startColor = Color(0x8026A69A)
        ) {
            Text(
                text = "지금 장보면 아침 7시부터 배달!",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                color = Color.Black
            )
        }
    }
}
