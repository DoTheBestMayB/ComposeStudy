package com.dothebestmayb.composestudy.side_effects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.delay

/**
 * rememberUpdatedState는 LaunchedEffect를 다시 실행하도록 하는 것이 아니라, 최신 데이터를 참조하도록 설정하는 것임
 * 예를 들어, 아래 코드에서 5초가 지나기 전과 후의 userName이 다르다면, updatedUserName은 항상 최신 userName을 가리킴
 */
@Composable
fun LaunchAnimation(
    snackbarHostState: SnackbarHostState,
    userName: String,
) {
    // key로 true를 전달하면 userName이 변경돼도 반영되지 않는다.(초기 "" 값을 출력함)
    // 아래와 같이 설정하면, userName이 변경될 때, 이전 코루틴 블록이 취소되고 변경된 userName이 반영된 코루틴 블록이 다시 실행된다.
    // 그런데, 다시 실행될 때마다 5초를 기다려야 한다.
    // 여기서는 단순히 5초 대기이지만, 만약 애니메이션을 보여주는 것이고, 단 한 번만 보여줘야 한다면 어떻게 해야 할까?
    // 이러한 상황에 사용하는 것이 rememberUpdatedState 이다.
//    LaunchedEffect(userName) {
//        delay(5000L)
//        snackbarHostState.showSnackbar(
//            "welcome to the app, $userName"
//        )
//    }

    val updatedUsername by rememberUpdatedState(userName)
    LaunchedEffect(true) {
        delay(5000L)
        snackbarHostState.showSnackbar(
            "welcome to the app, $updatedUsername"
        )
    }
}

@Composable
fun RememberUpdatedStateDemo(modifier: Modifier = Modifier) {
    var username by remember {
        mutableStateOf("")
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LaunchAnimation(
                snackbarHostState = snackbarHostState,
                userName = username
            )
            TextField(
                value = username,
                onValueChange = {
                    username = it
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray),
                placeholder = {
                    Text("Enter Something")
                }
            )
        }
    }
}

@Preview
@Composable
private fun RememberUpdatedStateDemoPreview() {
    ComposeStudyTheme {
        RememberUpdatedStateDemo()
    }
}
