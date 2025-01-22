package com.dothebestmayb.composestudy.measurements.home_work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * mainContent는 항상 표시
 * overFlowContent는 isOverflowing이 true일 때만 mainContent 아래에 표시
 *
 * The overflowContent should only be measured when visible -> subcompose
 */
@Composable
fun OverflowLayout(
    isOverflowing: Boolean,
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit,
    overflowContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
    ) {
        mainContent()
        if (isOverflowing) {
            overflowContent()
        }
    }
}

/**
 * Recap
 *
 * 1. subcompose를 이용해 measurable로 변경하기
 * 2. measure 함수를 시용해 크기가 측정된 Placeable로 변경하기
 * 3. Placeable 중 최대 width, height 측정하기
 * 4. layout 함수를 호출하며 Layout의 크기 설정하기
 * 5. place 함수를 이용해 각 placeable 배치하기
 */
@Composable
fun OverflowLayoutAnswer(
    isOverflowing: Boolean,
    modifier: Modifier = Modifier,
    mainContent: @Composable () -> Unit,
    overflowContent: @Composable () -> Unit
) {
    SubcomposeLayout(
        modifier = modifier,
    ) { constraints ->
        val overflowPlaceables = if (isOverflowing) {
            val overflowMeasurables = subcompose("overflow_content", overflowContent)
            overflowMeasurables.map { it.measure(constraints) }
        } else emptyList()

        val mainMeasurables = subcompose("main_content", mainContent)
        val mainPlaceables = mainMeasurables.map {
            it.measure(constraints)
        }

        val maxMainPlaceableWidth = mainPlaceables.maxOfOrNull { it.width } ?: 0
        val maxMainPlaceableHeight = mainPlaceables.maxOfOrNull { it.height } ?: 0
        val maxOverflowPlaceableWidth = overflowPlaceables.maxOfOrNull { it.width } ?: 0
        val maxOverflowPlaceableHeight = overflowPlaceables.maxOfOrNull { it.height } ?: 0

        val width = maxOf(maxMainPlaceableWidth, maxOverflowPlaceableWidth)
        val height = maxMainPlaceableHeight + maxOverflowPlaceableHeight

        layout(width, height) {
            mainPlaceables.fastForEach { placeable ->
                placeable.place(0, 0)
            }
            overflowPlaceables.forEach { placeable ->
                placeable.place(0, maxMainPlaceableHeight)
            }
        }
    }
}

@Preview
@Composable
private fun OverflowLayoutPreview() {
    ComposeStudyTheme {
        var isOverFlowing by remember {
            mutableStateOf(true)
        }
        var areaSize by remember {
            mutableStateOf(IntSize.Zero)
        }
        val density = LocalDensity.current

        OverflowLayoutAnswer(
            isOverflowing = isOverFlowing,
            mainContent = {
                Row(
                    modifier = Modifier
                        .onSizeChanged { newSize ->
                            areaSize = newSize
                        }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "this is a toggle section",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            isOverFlowing = !isOverFlowing
                        }
                    ) {
                        Icon(
                            imageVector = if (!isOverFlowing) {
                                Icons.Default.KeyboardArrowDown
                            } else {
                                Icons.Default.KeyboardArrowUp
                            },
                            contentDescription = null,
                        )
                    }
                }
            },
            overflowContent = {
                Text(
                    text = "Secret section",
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(with(density) {
                            areaSize.width.toDp()
                        })
                        .background(Color.Yellow)
                        .padding(16.dp)
                )
            }
        )
    }
}
