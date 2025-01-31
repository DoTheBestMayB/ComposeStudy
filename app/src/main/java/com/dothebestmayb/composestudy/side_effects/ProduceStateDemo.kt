package com.dothebestmayb.composestudy.side_effects

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.delay

/**
 * 1초마다 1씩 증가하는 카운터를 만들고자 함
 * 아래와 같이 state와 suspend block을 이용해서 구현할 수 있다.
 *
 * produceState를 이용하면 더 간단하게 구현 가능함
 * 초기 값을 전달하고, suspend block에 값에 대한 조건 문을 설정하면 된다.
 *
 * produceState 내부 코드를 확인해보면 state와 LaunchedEffect를 사용하고 있음
 *
 * 그런데 카운터를 구현하고 싶다면 UI에서 하는 것이 아니라, ViewModel에서 로직을 구현하고
 * UI에서는 State로 관찰해서 반영만 하는 것이 적합하다.
 * 여기서는 예시를 위한 코드임.
 */
@Composable
fun ProduceStateDemo(modifier: Modifier = Modifier) {
//    var counter by remember {
//        mutableIntStateOf(0)
//    }
//    LaunchedEffect(true) {
//        while (true) {
//            delay(1000L)
//            counter++
//        }
//    }
    val counter by produceState(0) {
        while (true) {
            delay(1000L)
            value += 1
        }
    }

    Text(
        text = counter.toString(),
        modifier = Modifier // 텍스트를 정가운데에 배치할 수 있음
            .fillMaxSize()
            .wrapContentSize()
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun ProduceStateDemoPreview() {
    ComposeStudyTheme {
        ProduceStateDemo()
    }
}
