package com.dothebestmayb.composestudy.state_management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * State Full Composable : Composable에서 필요로 하는 state를 Composable 내부에서 관리
 * Stateless Composable: Composable이 초기 상태 그대로 계속 유지되는 경우
 * State Hoisting : Composable에서 필요로 하는 State와 사용자 action에 따른 State 변경을 파라미터로 입력받아 외부에서 관리하도록 하는 것
 *
 * State 관리는 최대한 high hierarchy에서 관리하도록 하는 것이 좋다.
 *
 * event는 bubble up, state는 bubble down
 * 이것을 Unidirectional data flow(UDF)라고 부른다.
 */

@Composable
fun StateHoisting(modifier: Modifier = Modifier) {
    var counter by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StateHoistingCounter(
            counter = counter,
            onCounterButtonClick = {
                counter++
            }
        )
        Button(
            onClick = {
                counter = 0
            }
        ) {
            Text("Reset counter")
        }
    }
}

@Composable
fun StateFullCounter(modifier: Modifier = Modifier) {
    var countStateRemember by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                countStateRemember++
            }
        ) {
            Text("Count: $countStateRemember")
        }
    }
}


@Composable
fun StateHoistingCounter(
    counter: Int,
    onCounterButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = onCounterButtonClick
        ) {
            Text("Count: $counter")
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
private fun StateHoistingPreview() {
    ComposeStudyTheme {
        StateHoisting()
    }
}