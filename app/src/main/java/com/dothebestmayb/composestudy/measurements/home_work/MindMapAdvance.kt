@file:OptIn(ExperimentalFoundationApi::class)

package com.dothebestmayb.composestudy.measurements.home_work

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.round
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMapIndexedNotNull
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlin.math.roundToInt

data class MindMapItem(
    val offset: Offset,
    val content: @Composable () -> Unit,
    val constraints: Constraints,
)

private data class ProcessMindMapItem(
    val placeable: Placeable,
    val finalXPosition: Int,
    val finalYPosition: Int,
)

@Composable
fun MindMapAdvance(
    items: List<MindMapItem>,
    onDrag: (delta: IntOffset) -> Unit,
    modifier: Modifier = Modifier,
    mindMapOffset: IntOffset = IntOffset.Zero,
) {
    LazyLayout(
        modifier = modifier
            .draggable2D(
                state = rememberDraggable2DState { delta ->
                    onDrag(delta.round())
                }
            ),
        itemProvider = {
            object : LazyLayoutItemProvider {
                override val itemCount: Int
                    get() = items.size

                @Composable
                override fun Item(index: Int, key: Any) {
                    val item = items[index]
                    Layout(
                        content = item.content,
                        measurePolicy = { measurables, _ ->
                            val placeables = measurables.map {
                                it.measure(item.constraints)
                            }
                            val maxWidth = placeables.maxOfOrNull { it.width } ?: 0
                            val maxHeight = placeables.maxOfOrNull { it.height } ?: 0

                            layout(maxWidth, maxHeight) {
                                placeables.fastForEach {
                                    it.place(0, 0)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val visibleArea = IntRect(
            left = 0,
            top = 0,
            right = layoutWidth,
            bottom = layoutHeight,
        )

        // measure pass
        val visibleItems = items.fastMapIndexedNotNull { index, item ->
            // + 격자 모양의 좌표 상에서 몇 번째 화면의 x 좌표 값
            // + 화면의 정가운데를 0, 0으로 가정하기 위해 화면의 가로축 절반 크기
            // + 사용자가 보고 있는 화면의 x 좌표
            val finalXPosition =
                (item.offset.x + layoutWidth / 2 + mindMapOffset.x).roundToInt()
            val finalYPosition =
                (item.offset.y + layoutHeight / 2 + mindMapOffset.y).roundToInt()

            val maxItemWidth = item.constraints.maxWidth
            val maxItemHeight = item.constraints.maxHeight

            // 화면에서 보이지 않게 되더라도, 다시 드래그해서 볼 수 있으므로 화면 크기에서 여유 공간을 추가함
            val extendedItemBounds = IntRect(
                left = finalXPosition - maxItemWidth / 2,
                top = finalYPosition - maxItemHeight / 2,
                right = finalXPosition + 3 * (maxItemWidth / 2),
                bottom = finalYPosition + 3 * (maxItemHeight / 2),
            )

            if (visibleArea.overlaps(extendedItemBounds)) {
                // measure의 리턴 값이 list인 이유는 LazyLayoutItemProvider의 item 함수에서 여러 개의 Composable을 선언할 수 있기 때문이다.
                val placeable = measure(
                    index = index,
                    constraints = item.constraints
                ).first()

                ProcessMindMapItem(
                    placeable = placeable,
                    finalXPosition = finalXPosition,
                    finalYPosition = finalYPosition,
                )
            } else {
                null
            }
        }

        // layout pass
        layout(constraints.maxWidth, constraints.maxHeight) {
            visibleItems.fastForEach { item ->
                item.placeable.place(
                    x = item.finalXPosition - item.placeable.width / 2,
                    y = item.finalYPosition - item.placeable.height / 2,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun MindMapAdvancePreview() {
    ComposeStudyTheme {
        var count by remember {
            mutableIntStateOf(0)
        }

        val mindMapItems = remember<List<MindMapItem>> {
            listOf(
                MindMapItem(
                    offset = Offset.Zero,
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text("counter is $count")
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                Button(
                                    onClick = {
                                        count++
                                    }
                                ) {
                                    Text("Increase")
                                }
                                Button(
                                    onClick = {
                                        count--
                                    }
                                ) {
                                    Text("Decrease")
                                }
                            }
                        }
                    },
                    constraints = Constraints(
                        maxWidth = 2000,
                        maxHeight = 1500
                    )
                ),
                MindMapItem(
                    offset = Offset(
                        x = -700f,
                        y = -700f,
                    ),
                    content = {
                        var check by remember {
                            mutableStateOf(false)
                        }
                        Row {
                            Column {
                                Text(
                                    text = "Mind Map Todo",
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (check) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    },
                                )
                                Text(
                                    text = "Description",
                                    textDecoration = if (check) {
                                        TextDecoration.LineThrough
                                    } else {
                                        TextDecoration.None
                                    }
                                )
                            }
                            Column {
                                Checkbox(
                                    checked = check,
                                    onCheckedChange = {
                                        check = it
                                    }
                                )
                            }
                        }
                    },
                    constraints = Constraints(
                        maxWidth = 2000,
                        maxHeight = 1500
                    )
                ),
            )
        }

        var mindMapOffset by remember {
            mutableStateOf(IntOffset.Zero)
        }
        MindMapAdvance(
            items = mindMapItems,
            mindMapOffset = mindMapOffset,
            onDrag = { delta ->
                mindMapOffset += delta
            },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
