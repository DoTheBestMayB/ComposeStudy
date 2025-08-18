package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * performance issue가 있다면, release build에서도 그러한지 확인하자.
 * Build -> Select Build Variant -> debug(default)를 release로 변경
 *
 * debug build에서는 더 많은 로그를 남기기 위한 연결 등 android studio와 관련된 추가적인 작업이 수행된다.
 * release build에서 이슈가 발생하지 않는다면 무시해도 좋다.
 *
 * 성능 문제의 원인을 확인하기 위한 tool
 * 1. layout inspector
 */


@Composable
private fun MyScreen(modifier: Modifier = Modifier) {
    var counter by remember {
        mutableIntStateOf(0)
    }
    MyCounter(
        counter = counter,
        onClick = { counter++ },
        modifier = modifier,
    )
}

@Composable
fun MyCounter(
    counter: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text("Counter: $counter")
    }
}


@Preview
@Composable
private fun ProfilePerformancePreview() {
    ComposeStudyTheme {
        Scaffold {
            MyScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}
