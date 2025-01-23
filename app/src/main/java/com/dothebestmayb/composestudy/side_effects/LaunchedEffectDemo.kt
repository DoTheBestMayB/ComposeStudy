package com.dothebestmayb.composestudy.side_effects

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * counter가 짝수일 때 Snackbar를 보여주고자 한다. Snackbar를 보여주는 것은 SideEffect에 해당한다.
 * SideEffect는 어떻게 처리할 수 있을까?
 *
 * 1. onClick, onValueChanged와 같은 eventHandler 람다 블록 내부에서 처리한다.
 * 이벤트 핸들러는 사용자와 상호작용하는 시점에만 실행되고, Recomposition과는 독립적으로 실행되기 때문에 괜찮다.
 *
 * 2. LaunchedEffect
 *
 *
 * LaunchedEffect는 key 값을 입력받아, key가 변경될 때 코루틴 블록을 다시 실행한다.
 * key는 compose state여야 한다. 일반 변수를 key로 입력할 경우 Compose는 변경을 추적하지 못하기 때문에
 * 값이 변경되더라도 코루틴 블록이 다시 실행되지 않을 수 있다.
 *
 * LaunchedEffect의 코루틴 블록은 composable이 아님
 *
 * LaunchedEffect는 key가 변경되어 다시 실행될 때, 이전에 실행하던 Job을 취소하고, 다시 실행한다.
 * 그래서 방법 1을 실행할 때는 snackbar가 사라진 후에 다음 snackbar가 나타났지만
 * LaunchedEffect에서는 counter가 짝수일 때 보여진 snackbar가 counter가 홀수로 변경되는 순간 감춰진다.
 *
 * 즉 Flow의 collectLatest 함수와 같이 동작한다고 볼 수 있다.
 */
@Composable
fun LaunchedEffectDemo(modifier: Modifier = Modifier) {
    var counter by remember {
        mutableIntStateOf(0)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    // 현재 Composable의 lifeycle을 따르는 coroutine scope을 얻는 방법
    // Composable이 compose hierachy에서 제거되면 scope에서 실행되던 코루틴은 모두 취소된다.
    val scope = rememberCoroutineScope()

    // 아래와 같은 코드는 작성하면 안 된다.
    // launch, showSnackbar 함수 모두 일반 함수로써, sideEffect에 해당한다.
//    scope.launch {
//        snackbarHostState.showSnackbar(
//            "The number is even!"
//        )
//    }

    // 방법 2 : LaunchedEffect
    LaunchedEffect(counter) {
        if (counter % 2 == 0) {
            snackbarHostState.showSnackbar(
                "The count is even!"
            )
        }
    }

    // LaunchedEffect가 유용한 경우 1. 특정 상태에서 코드를 실행해야 할 때
    // ex) 특정 페이지에서 추가 데이터를 로딩(비추천하는 예시임. 이것은 viewModel에서 처리하는 것이 더 바람직)
//    val pagerState = rememberPagerState {  }
//    LaunchedEffect(pagerState.currentPage) {
//
//    }

    // LaunchedEffect가 유용한 경우 2. composition이 완료되는 시점에 한 번만 실행해야 할 때
    // ex) 권한 요청
    // key 값은 true, Unit, 그 외 어떤 값이든 상관없다. constant 값을 입력하면 한 번만 실행된다.
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//
//    }
//    LaunchedEffect(true) {
//        launcher.launch(android.Manifest.permission.RECORD_AUDIO)
//    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        Button(

            onClick = {
                counter++
                /**
                 * 방법 1 : eventHandler lambda block
                 */
//                if (counter % 2 == 0) {
//                    // showSnackbar는 suspend function이기 때문에 coroutine scope 필요
//                    scope.launch {
//                        snackbarHostState.showSnackbar(
//                            "The number is even!"
//                        )
//                    }
//                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .wrapContentSize()
        ) {
            Text("Counter : $counter")
        }
    }
}

@Preview
@Composable
private fun LaunchedEffectDemoPreview() {
    ComposeStudyTheme {
        LaunchedEffectDemo()
    }
}
