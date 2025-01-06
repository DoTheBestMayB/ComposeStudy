@file:OptIn(ExperimentalFoundationApi::class)

package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlin.math.roundToInt

enum class DragAnchors {
    RESTING,
    End,
}

/**
 * AnchoredDraggable 관련 Compose Tips( 채팅 화면에서 스와이프로 답장하기)
 * https://www.youtube.com/watch?v=JYtLy4V2x-A
 */
@Composable
fun DraggableModifiersDemo(modifier: Modifier = Modifier) {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val anchorDragState = remember {
        AnchoredDraggableState<DragAnchors>(
            initialValue = DragAnchors.RESTING,
            anchors = DraggableAnchors {
                DragAnchors.RESTING at 0f
                DragAnchors.End at 300f
            },
            positionalThreshold = { density ->
                density * 0.5f
            },
            velocityThreshold = {
                with(density) {
                    100.dp.toPx()
                }
            },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                // offset과 draggable 설정은 clip, background 보다 앞에 와야 함
//                .offset {
//                    offset.round()
//                }
                // 상하 또는 좌우 1개 축으로만 옮길 수 있도록 함
//                .draggable(
//                    state = rememberDraggableState {
//                        offset += Offset(
//                            x = it, // Vertical이면 x를 0, y를 it
//                            y = 0f,
//                        )
//                    },
//                    orientation = Orientation.Horizontal
//                )
                // 2차원 평면에 자유롭게 옮길 수 있도록 함
//                .draggable2D(
//                    state = rememberDraggable2DState {
//                        offset += it
//                    }
//                )
                .offset {
                    IntOffset(
                        x = anchorDragState.requireOffset().roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    state = anchorDragState,
                    orientation = Orientation.Horizontal,
                )
                .clip(CircleShape)
                .background(Color.Red)
        )
    }
}

@Preview
@Composable
private fun DraggableModifiersDemoPreview() {
    ComposeStudyTheme {
        DraggableModifiersDemo()
    }
}
