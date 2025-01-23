package com.dothebestmayb.composestudy.side_effects

import androidx.compose.material3.Button
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
 * sideEffect : composable function 내부에서 composable function이 아닌 일반 function을 호출하는 것
 *
 * sideEffect free한 코드를 작성해야 하는 이유 : composable function이 언제, 어떻게 호출되는지(recomposition에 의해서 포함) 정확히 제어하기 위해서
 */
@Composable
fun SideEffectsDemo(modifier: Modifier = Modifier) {
    var counter by remember {
        mutableIntStateOf(0)
    }
    // 아래와 같은 코드는 SideEffect. navigate는 composable function이 아니기 때문
//    if (counter == 5) {
//        navController.navigate(ScreenB)
//    }

    Button(
        onClick = {
            counter++
        }
    ) {
        // counter++ 또한 sideEffect. 내부적으로 Int 클래스의 Inc 함수를 호출한다.
//        counter++
        Text("Counter: $counter")
    }
}

@Preview
@Composable
private fun SideEffectDemoPreview() {
    ComposeStudyTheme {
        SideEffectsDemo()
    }
}
