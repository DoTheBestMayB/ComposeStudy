@file:OptIn(ExperimentalFoundationApi::class)

package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.round
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMapIndexedNotNull
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlin.math.roundToInt

data class MindMapItem(
    val title: String,
    // 화면 크기를 가진 격자 모양의 좌표상에서, 몇 번째 좌표 화면에 있는지 나타내는 변수
    val percentageOffset: Offset,
)

private data class ProcessMindMapItem(
    val placeable: Placeable,
    val finalXPosition: Int,
    val finalYPosition: Int,
)

@Composable
fun LazyMindMap(
    items: List<MindMapItem>,
    onDrag: (delta: IntOffset) -> Unit,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
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
                    Text(
                        text = items[index].title,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = itemModifier
                            .widthIn(min = 50.dp, max = 150.dp)
                            .heightIn(min = 50.dp, max = 75.dp)
                            .border(
                                width = 2.dp,
                                color = Color.LightGray,
                            )
                            .padding(16.dp)
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
                (item.percentageOffset.x * layoutWidth + layoutWidth / 2 + mindMapOffset.x).roundToInt()
            val finalYPosition =
                (item.percentageOffset.y * layoutHeight + layoutHeight / 2 + mindMapOffset.y).roundToInt()

            val maxItemWidth = 150.dp.roundToPx()
            val maxItemHeight = 75.dp.roundToPx()

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
                    constraints = Constraints() // Item의 modifier에 따라 달라지므로 기본 값으로 둠
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

        println("visible item count: ${visibleItems.size}")

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
private fun LazyMindMapPreview() {
    ComposeStudyTheme {
        val mindMapItems = remember {
            listOf(
                MindMapItem(
                    title = "Hello World 1",
                    percentageOffset = Offset(
                        x = 0f,
                        y = 0f,
                    )
                ),
                MindMapItem(
                    title = "Hello World 2",
                    percentageOffset = Offset(
                        x = 1f,
                        y = 0f,
                    )
                ),
                MindMapItem(
                    title = "Hello World 3",
                    percentageOffset = Offset(
                        x = 0.3f,
                        y = -0.5f,
                    )
                ),
                MindMapItem(
                    title = "Hello World 4",
                    percentageOffset = Offset(
                        x = -0.2f,
                        y = 1.5f,
                    )
                ),
            )
        }

        var mindMapOffset by remember {
            mutableStateOf(IntOffset.Zero)
        }
        LazyMindMap(
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
