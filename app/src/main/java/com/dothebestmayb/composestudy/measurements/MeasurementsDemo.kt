package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme


private val boundedConstraints = Constraints(
    minWidth = 50,
    maxWidth = 100,
    minHeight = 70,
    maxHeight = 150,
)

private val unboundedConstraints = Constraints()

private val exactConstraints = Constraints(
    minWidth = 50,
    maxWidth = 50,
    minHeight = 100,
    maxHeight = 100,
)

// 고정 값 + 변동 값
private val combinedConstraints = Constraints(
    minWidth = 50,
    maxWidth = 50,
    minHeight = 50,
    maxHeight = Constraints.Infinity,
)

/**
 * Column, Row, LazyColumn과 같은 Layout의 목적 중 하나는 child 위치를 measure 하는 것.
 * 모든 Composable은 Layout이다. Text 또한 Layout이다.
 * Text의 내부 코드를 따라가보면 BasicText -> Layout이 나오고
 * Layout에서 ReusableComposeNode가 사용되고 있다.
 *
 * content에 있는 composable 뿐만 아니라, 일부 Modifier도 Layout이다.
 * ex) padding modifier
 *
 * Composable이 그려질 때 3가지 과정을 거친다.
 * 1. Composition - What: 어떤 것을 그려야 할지 결정하는 단계
 * 2. Layout phase - Where: Layout의 child composable의 크기와 위치를 계산하는 단계
 * 3. drawing phase - How: 어떻게 그릴 것인지 결정하는 단계
 *
 * 여기서는 Layout phase - where에 대해 알아본다.
 * Layout phase 또한 3가지 과정을 거친다.
 * 1. mesaure child : child의 크기를 측정한다.
 * 2. measure itself(layout) : layout 자신의 크기를 측정한다.
 * 3. place child : child를 배치한다.
 *
 * measure child 과정에서 Constraints를 layout에 전달한다. 그 과정은 다음과 같다.
 * Column이 최상위 Layout이라고 가정
 * 1. Column은 unboundedConstraints인 Constraints() 값을 가진다.
 * 2. 크기에 영향을 주는 가장 상위에 있는 child Layout인 padding modifier에게 Constraints를 전달한다.
 * 3. padding은 minWidth와 minHeight에 padding값의 2배를 추가한다. 그래서 Constraints(32.dp, 32.dp)가 된다.
 * 4. 다음으로 Text가 Constraints를 전달받고, 자신의 Text 크기만큼 min width, height를 증가시킨다.
 */
@Composable
fun MeasurementsDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .padding(16.dp)
    ) {
        Text(
            text = "This is a text",
            modifier = Modifier.
            background(Color.Yellow)
        )
        Text(
            text = "This is another text",
            modifier = Modifier
                .background(Color.Green)
            )
    }
}

@Preview
@Composable
private fun MeasurementsDemoPreview() {
    ComposeStudyTheme {
        MeasurementsDemo()
    }
}
