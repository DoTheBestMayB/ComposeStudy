@file:OptIn(ExperimentalLayoutApi::class, ExperimentalAnimatableApi::class)

package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.ApproachMeasureScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * 여러 개의 child가 있는 Layout에서 child의 크기가 변경되는 animation이 적용될 때, 성능상 주의가 필요하다.
 * 단순히 animation을 적용하면 변경된 크기에 따라 각 child의 위치가 변경되기 때문에 매 프레임마다 layout을 다시 측정해야 한다.
 *
 * LookaheadScope : Layout에 있는 Item이 Animation에 의해 변경되기 전, 해당 Item의 최종 Position과 Size가 어떻게 되는지 미리 확인한다(lookahead pass)
 * 그리고 확인한 값을 이용해 애니메이션을 적용한다.(approach pass) 이때, measure pass와 layout pass는 오직 1번만 발생한다.
 *
 * ```Text
 * A look ahead layout is exactly what the name says it tries to look ahead when there is a certain change of
 * that layout, like, when we increased the size, or the change, the
 * positions of the items in the layout, it tries to look ahead. What the
 * end state of that animation will be And then provide us those values
 * that that final size and that final Target position that we want to
 * animate towards, and let us perform that animation.
 * ```
 *
 * LookaheadScope의 장점 : 기존 Composable을 LookaheadScope으로 wrapping 하고, modifier만 설정하면 된다.
 * 즉, 기존 코드에 영향을 주지 않는다.
 */
@Composable
private fun LookaheadLayoutAnimations(modifier: Modifier = Modifier) {
    var horizontalArrangement by remember {
        mutableStateOf(Arrangement.Start)
    }
    var dpIncrement by remember {
        mutableStateOf(0.dp)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnimatedFlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = horizontalArrangement
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp + dpIncrement)
                    .animateLayoutChanges(this)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .size(100.dp + dpIncrement)
                    .animateLayoutChanges(this)
                    .background(Color.Green)
            )
            Box(
                modifier = Modifier
                    .size(100.dp + dpIncrement)
                    .animateLayoutChanges(this)
                    .background(Color.Blue)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.Start
                }
            ) {
                Text("Start")
            }
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.Center
                }
            ) {
                Text("Center")
            }
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.End
                }
            ) {
                Text("End")
            }
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.SpaceBetween
                }
            ) {
                Text("SpaceBetween")
            }
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.SpaceAround
                }
            ) {
                Text("SpaceAround")
            }
            Button(
                onClick = {
                    horizontalArrangement = Arrangement.SpaceEvenly
                }
            ) {
                Text("SpaceEvenly")
            }
            Button(
                onClick = {
                    dpIncrement += 10.dp
                }
            ) {
                Text("Inc DP")
            }
            Button(
                onClick = {
                    dpIncrement -= 10.dp
                }
            ) {
                Text("Dec DP")
            }
        }
    }
}

@Composable
fun AnimatedFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable LookaheadScope.() -> Unit,
) {
    LookaheadScope {
        FlowRow(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = horizontalArrangement,
        ) {
            content()
        }
    }
}

fun Modifier.animateLayoutChanges(
    scope: LookaheadScope,
    positionAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D>  = DeferredTargetAnimation(IntOffset.VectorConverter),
    sizeAnimation: DeferredTargetAnimation<IntSize, AnimationVector2D>  = DeferredTargetAnimation(IntSize.VectorConverter),
): Modifier {
    return then(AnimateLayoutChangesElement(
        scope = scope,
        positionAnimation = positionAnimation,
        sizeAnimation = sizeAnimation,
    ))
}

private data class AnimateLayoutChangesElement(
    val scope: LookaheadScope,
    val positionAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D>,
    val sizeAnimation: DeferredTargetAnimation<IntSize, AnimationVector2D>,
): ModifierNodeElement<AnimateLayoutChangesNode>() {

    override fun create(): AnimateLayoutChangesNode {
        return AnimateLayoutChangesNode(
            scope = scope,
            positionAnimation = positionAnimation,
            sizeAnimation = sizeAnimation
        )
    }

    override fun update(node: AnimateLayoutChangesNode) = Unit
}

private class AnimateLayoutChangesNode(
    private val scope: LookaheadScope,
    private val positionAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D>,
    private val sizeAnimation: DeferredTargetAnimation<IntSize, AnimationVector2D>,
) : ApproachLayoutModifierNode, Modifier.Node() {

    override fun Placeable.PlacementScope.isPlacementApproachInProgress(
        lookaheadCoordinates: LayoutCoordinates
    ): Boolean {
        val targetOffset = with(scope) {
            lookaheadScopeCoordinates.localLookaheadPositionOf(lookaheadCoordinates)
        }
        positionAnimation.updateTarget(targetOffset.round(), coroutineScope, tween(1500))

        return !positionAnimation.isIdle
    }

    override fun isMeasurementApproachInProgress(lookaheadSize: IntSize): Boolean {
        sizeAnimation.updateTarget(lookaheadSize, coroutineScope, tween(1500))
        return !sizeAnimation.isIdle
    }

    override fun ApproachMeasureScope.approachMeasure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val (width, height) = sizeAnimation.updateTarget(
            target = lookaheadSize,
            coroutineScope = coroutineScope,
            animationSpec = tween(1500),
        )
        // minimum, maximum 값이 width, height로 설정된 Constraints 생성
        val animatedConstraints = Constraints.fixed(width, height)
        val placeable = measurable.measure(animatedConstraints)

        return with(scope) {
            layout(placeable.width, placeable.height) {
                coordinates?.let {
                    val targetOffset = lookaheadScopeCoordinates.localLookaheadPositionOf(it)
                    val animatedOffset = positionAnimation.updateTarget(
                        target = targetOffset.round(),
                        coroutineScope = coroutineScope,
                        animationSpec = tween(1500),
                    )

                    val currentOffset = lookaheadScopeCoordinates.localPositionOf(
                        sourceCoordinates = it,
                    )
                    val (x, y) = animatedOffset - currentOffset.round()

                    placeable.place(x, y)
                } ?: placeable.place(0, 0)
            }
        }
    }
}

@Preview
@Composable
private fun LookaheadLayoutAnimationsPreview() {
    ComposeStudyTheme {
        LookaheadLayoutAnimations()
    }
}
