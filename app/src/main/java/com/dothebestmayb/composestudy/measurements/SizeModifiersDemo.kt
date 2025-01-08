package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.measurements.utility.printConstraints
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * ### 왜 size를 basic modifier가 아닌 여기서 설명할까?
 * deep dive 하면서 이해하기 위해 이전 강의에서 배운 Constraints 개념이 필요하기 때문이다.
 *
 * ### fillMaxWidth는 어떻게 동작하는 것일까?
 * parent 또는 앞선 순서에 있는 sibiling layout으로 부터 전달된 Constraints를 조작하는데
 * minwidth를 maxwidth 값으로 변경한다.
 *
 * fraction 파라미터로 전달하는 값은 minWidth가 변경될 값에 영향을 미친다.
 * minWidth = maxWidth * fraction
 *
 * 아무것도 설정하지 않으면 기본 Constraints는 아래와 같다.
 * ```kotlin
 * private val con = Constraints(
 *     minWidth = 0,
 *     maxWidth = 800, // 화면 width 크기
 *     minHeight = 0,
 *     maxHeight = 1200, // 화면 height 크기
 * )
 * ```
 *
 */
@Composable
fun SizeModifierDemo(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            // Constraints(minWidth = 0, maxWidth = 1080, minHeight = 263, maxHeight = 263)
            .printConstraints("Before 0. fillMaxWidth")
            // case 1 : 여기서 fillMaxWidth를 설정하지 않으면 Row는 스크린 크기만큼 차지함
            // 또한 child layout에서 maxWidth를 조정하면 해당 크기만큼 차지하는 width 공간이 줄어듬
            // 이것은 실행해보면 빨간색 배경이 사라지는 것을 통해 확인할 수 있음
//            .fillMaxWidth()
            // case 2 : minWidth, maxWidth를 300.dp로 설정함
            // dp는 화면 해상도에 따라 변환되는 픽셀 값이 달라지는데, 공식은 아래와 같다.
            // px = dp * (dpi / 160)
            // density를 이용해 변환하면 다음과 같다. 0.5f를 더하는 이유는 반올림 하기 위해서다.
            // px = (dp * density + 0.5f).toInt()
            // 출처 : https://developer.android.com/training/multiscreen/screendensities#dips-pels
            .width(300.dp)
            // Constraints(minWidth = 788, maxWidth = 788, minHeight = 263, maxHeight = 263)
            .printConstraints("After 0. fillMaxWidth")
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                // Constraints(minWidth = 0, maxWidth = 1080, minHeight = 263, maxHeight = 263)
                .printConstraints("Before 1. fillMaxWidth")
                .fillMaxWidth(0.5f)
                // Constraints(minWidth = 540, maxWidth = 540, minHeight = 263, maxHeight = 263)
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow)
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                // horizontal dimension 상에서 min 값은 초기화 된다.
                // 따라서 이전 Box Modifier에서 minWidth를 540으로 설정해도 0으로 바뀐 것이다.
                // max 값이 540인 이유는 이전 modifier에서 width의 50%를 차지하기 때문이다.
                // Constraints(minWidth = 0, maxWidth = 540, minHeight = 263, maxHeight = 263)
                .printConstraints("Before 2. fillMaxWidth")
                .fillMaxWidth(0.5f)
                .printConstraints("After 2. fillMaxWidth")
                .background(Color.Green)
        )
    }
}

/**
 *  ### width는 어떻게 동작하는 것일까?
 *  width의 설명을 보면 아래와 같이 되어 있다.
 *  ```text
 *  Declare the preferred width of the content to be exactly [width]dp.
 *  ```
 *
 *  왜 preferred 인가? width의 파라미터로 설정한 dp보다 남은 공간이 작을 경우
 *  남은 공간 만큼만 minWidth와 maxWidth를 설정하기 때문이다.
 *
 *  따라서 modifier의 순서도 중요한데, 아래와 같이 작은 값을 설정하고, 큰 값을 설정하면 Constraints는 변경되지 않는다.
 *  ```kotlin
 *  Modifier
 *     .width(300.dp)
 *     .width(500.dp)
 * ```
 */
@Composable
fun WidthExample(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                // Constraints(minWidth = 0, maxWidth = 1080, minHeight = 263, maxHeight = 263)
                .printConstraints("Before 1. fillMaxWidth")
                .width(300.dp)
                // Constraints(minWidth = 788, maxWidth = 788, minHeight = 263, maxHeight = 263)
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow)
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                // 전체 길이 1080에서 이전 Box에서 차지하는 width 길이 788을 뺀 292만큼 최대 길이가 할당됨
                // Constraints(minWidth = 0, maxWidth = 292, minHeight = 263, maxHeight = 263)
                .printConstraints("Before 2. fillMaxWidth")
                .width(300.dp)
                // 300.dp를 설정했지만, 주어진 공간이 300.dp보다 작기 때문에 주어진 공간의 최대 길이만큼 설정함
                // fillMaxWidth: Constraints(minWidth = 292, maxWidth = 292, minHeight = 263, maxHeight = 263)
                .printConstraints("Before 2. fillMaxWidth")
                .background(Color.Green)
        )
    }
}

/**
 * widthIn은 다음에 오는 sibling layout의 Constraint가 더 많은 공간을 요구하면 해당 공간을 차지할 수 있도록 min에 가까운 공간만 차지하고
 * 더 적은 공간을 요구하면 남은 공간 중 최대 max까지 공간을 차지한다.
 *
 * 그런데, widthIn 이전에 width를 호출해서 minWidth, maxWidth를 설정해버리면 효과가 없다.
 *
 * ```kotlin
 * Modifier
 *  .width(300.dp)
 *  .widthIn(min = 100.dp, max = 500.dp) // no impact
 * ```
 */
@Composable
fun WidthInExample(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 1. fillMaxWidth")
//                .width(500.dp)
                .printConstraints("while 1. fillMaxWidth")
                .widthIn(min = 100.dp, max = 300.dp)
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow)
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 2. fillMaxWidth")
                .width(300.dp)
                .printConstraints("Before 2. fillMaxWidth")
                .background(Color.Green)
        )
    }
}

/**
 *
 * requireWidth는 남은 공간이 부족하더라도 해당 값만큼 차지하도록 강제로 min, maxWidth를 설정한다.
 * 아래 예시에서 두 box는 모두 300.dp만큼 width 공간을 가진다.
 * 그런데 녹색도 300.dp 만큼 width를 강제적으로 설정했기 떄문에 노란색 공간 위에 겹쳐져 그리게 됐다.
 *
 */
@Composable
fun RequireWidthExample(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 1. fillMaxWidth")
                .requiredWidth(300.dp)
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text("Hello World this is a text")
        }
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 2. fillMaxWidth")
                .requiredWidth(300.dp)
                .printConstraints("Before 2. fillMaxWidth")
                .background(Color.Green),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text("Hello World this is a text")
        }
    }
}

/**
 * wrapContentWidth는 Content의 크기에 맞는 최소한의 크기로 minWidth, maxWidth를 설정한다.
 * wrapContentWidth를 설정하더라도, 앞에서 modifier를 이용해 width 크기를 설정하면 해당 크기는 그대로 적용된다.
 *
 * 즉, wrapContentWidth는 modifier를 적용하고 있는 Layout이 아니라 content의 Constraints에 영향을 주는 것이다.
 */
@Composable
fun WrapContentExample(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 1. fillMaxWidth")
                .requiredWidth(300.dp)
                .wrapContentWidth()
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow),
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 2. fillMaxWidth")
                .requiredWidth(300.dp)
                .wrapContentWidth()
                .printConstraints("Before 2. fillMaxWidth")
                .background(Color.Green),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text("Hello World this is a text")
        }
    }
}

@Composable
fun WrapContentExample2(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .printConstraints("Before 1. fillMaxWidth")
                .requiredWidth(300.dp)
                .background(Color.Green)
                .wrapContentWidth(
                    align = Alignment.End
                )
                .printConstraints("After 1. fillMaxWidth")
                .background(Color.Yellow),
        ) {
            Text("Hello")
        }
    }
}

@Preview
@Composable
private fun SizeModifierDemoPreview() {
    ComposeStudyTheme {
        WrapContentExample2()
    }
}
