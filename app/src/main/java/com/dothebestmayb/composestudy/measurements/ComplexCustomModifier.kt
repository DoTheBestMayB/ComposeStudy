package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * 아래 함수의 문제점
 * 1. Composition Local에 대한 접근 : Composable function에서 Composition Local에 접근하면 inconsistency를 유발할 수 있다.
 * 2. return value - no skip : Composable function에 리턴 값이 있으면, recomposition시, 사용된 state의 변경 여부와 관계 없이 항상 다시 그린다.
 * 3. Composable 내부에서만 사용할 수 있다 : Composable이 아닌 일반 클래스에서는 사용할 수 없다.
 *
 * 위와 같은 문제를 Modifier factory로 해결할 수 있다. (Modifier factory를 이용한 함수는 아래에 있음)
 */
@Composable
fun Modifier.negativePadding(horizontal: Dp): Modifier {
    val density = LocalDensity.current
    val px = with(density) {
        horizontal.roundToPx()
    }

    return layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints = constraints.copy(
                // 양쪽에 padding을 적용하므로 2를 곱한 값을 더함
                minWidth = constraints.minWidth + 2 * px,
                maxWidth = constraints.maxWidth + 2 * px,
            )
        )

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

/**
 * Modifier factory는 Node를 이용해서 구현한다.
 * 이 Node를 이용하려면 Element가 필요하다. ( 아래에 있음 )
 */
class NegativePaddingNode(
    var horizontal: Dp,
) : LayoutModifierNode, Modifier.Node(), CompositionLocalConsumerModifierNode {

    /**
     * LayoutModifierNode를 구현하기 때문에 measure 함수를 overriding 한다.
     * DrawModifierNode를 구현하면 draw 함수를 override 한다.
     * 이 함수는 layout처럼 measurable과 constraints를 파라미터로 입력받는다.
     */
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        // Composable 함수가 아니기 때문에 Composition Local을 사용할 수 없다.
        // 대신 CompositionLocalConsumerModifierNode 인터페이스를 이용해 사용할 수 있다.
        val density = currentValueOf(LocalDensity)
        val px = with(density) {
            horizontal.roundToPx()
        }

        val placeable = measurable.measure(
            constraints = constraints.copy(
                // 양쪽에 padding을 적용하므로 2를 곱한 값을 더함
                minWidth = constraints.minWidth + 2 * px,
                maxWidth = constraints.maxWidth + 2 * px,
            )
        )

        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

/**
 * modifier를 설정하고, 언제 update 되는지 상세하게 설정할 수 있다.
 * property로 입력된 dp는 compose state로써 동작해야 한다. 이것을 update 함수에서 설정할 수 있다.
 */
data class NegativePaddingElement(
    private val horizontal: Dp
): ModifierNodeElement<NegativePaddingNode>() {

    override fun create(): NegativePaddingNode {
        return NegativePaddingNode(horizontal)
    }

    override fun update(node: NegativePaddingNode) {
        // node에 대한 measure를 다시 수행함
//        node.invalidateMeasurement()

        // horizontal이 변경된 경우에만 recomposition이 발생하도록 아래와 같이 수정함
        node.horizontal = horizontal
    }
}

fun Modifier.improvedNegativePadding(horizontal: Dp): Modifier {
    return this.then(NegativePaddingElement(horizontal))
}


@Composable
fun MyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        var toggle by remember {
            mutableStateOf(true)
        }

        Text(
            text = "Click Here to Toggle Negative Modifier",
            modifier = Modifier
                .clickable {
                    toggle = !toggle
                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .improvedNegativePadding(
                    if (toggle) 0.dp else 16.dp
                )
        )
        HorizontalDivider(
            modifier = Modifier
                .improvedNegativePadding(16.dp)
        )
        Text(
            text = "This is another item",
            modifier = Modifier
                .clickable {

                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .improvedNegativePadding(16.dp)
        )
        Text(
            text = "Add another one",
            modifier = Modifier
                .clickable {

                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                onClick = {}
            ) {
                Text("Button 1")
            }
            Button(
                onClick = {}
            ) {
                Text("Button 2")
            }
            Button(
                onClick = {}
            ) {
                Text("Button 3")
            }
        }
    }
}

@Preview
@Composable
private fun MyListPreview() {
    ComposeStudyTheme {
        MyList()
    }
}
