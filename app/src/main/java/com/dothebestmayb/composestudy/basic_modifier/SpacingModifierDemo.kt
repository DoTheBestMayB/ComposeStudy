@file:OptIn(ExperimentalLayoutApi::class)

package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * padding : child composable이 차지하는 영역을 줄이는 것
 * margin이 없는 이유 : padding으로 동일하게 구현할 수 있기 때문에
 * window Insets : Content가 시스템 UI(상단바, 내비게이션바)와 겹치지 않도록 하는 padding 값
 *
 * Scaffold를 사용하면 innerPadding으로 window Insets 값을 전달해준다
 * Scaffold를 사용하지 않는다면 Modifier.satatusBarPadding과 같이 직접 적용해주면 된다.
 */
@Composable
fun SpacingModifierDemo(modifier: Modifier = Modifier) {
    // 파라미터로 전달된 modifier를 최상단 composable modifier에 적용해야 한다.
    // 만약 이 composable이 MainActivity에서 시작되는 top-level composable이 될 경우,
    // 상단바와 content가 겹치지 않도록 innerPadding을 적용해야하기 때문이다.
    // innerPadding은 상단바 뿐만 아니라 내비게이션바 영역과도 겹치지 않도록 하는 값이다.
    Column(
        modifier = modifier
            .safeContentPadding()
            .fillMaxSize()
            .background(Color.Red)
            .padding(horizontal = 16.dp)
    ) {
        Text("Hello World!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Hello World!"
        )
        TextField(
            value = "",
            onValueChange = {},
        )
    }

    // innerPadding 없이 직접 window insets 처리하기
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            // 양쪽 끝 단에서 swipe할 경우, 시스템 기본 제스처가 적용될 수 있음
            // 이러한 기본 제스처 영역에 padding을 줘서 사용자가
            // 시스템 제스처와 앱 내 제스처를 구분해서 사용할 수 있도록 함
            // 상하단에도 padding이 적용된다. 상단의 경우 아래로 내리면 알림창이 나오는 제스처 영역과
            // 구분하기 위함임
            .safeGesturesPadding()
            // UI를 표시하기에 적절한 영역만 차지하도록 padding 값을 적용함
            // UI를 표시하는 것과 제스처는 무관하기 때문에 양쪽에는 padding이 적용되지 않음
            .safeDrawingPadding()
            // safeGesturePadding와 유사함. 다른 점은 키보드가 나오면 해당 영역도 padding 값으로 적용함
            .safeContentPadding()
    ) {

    }

    // 아래와 같이 WindowInsets를 적용할 수도 있다.
    Modifier.windowInsetsPadding(WindowInsets.ime)

    // WindowInsets는 중복되서 적용되지 않는다. Compose가 스마트하게 이미 적용된 것은 중복 적용하지 않는다.
    Modifier
        .safeGesturesPadding()
        .safeGesturesPadding()
        // 이 함수를 호출하면, 파라미터로 전달된 padding은 더 이상 호출해도 적용되지 않는다.
        .consumeWindowInsets(WindowInsets.statusBars)
}

/**
 * imeNestedScroll 사용 예시
 * LazyColumn에서 item의 마지막까지 내리면 ime 키보드가 나오도록 해준다.
 * 다시 위로 올리면 ime 키보드가 내려간다.
 */
@Composable
fun SpacingModifierLazyDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imeNestedScroll()
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
        TextField(
            value = "",
            onValueChange = {},
        )
    }
}

/**
 * scaffold와 window Insets를 함께 사용할 때 주의할 점
 */
@Composable
fun SpacingModifierScaffoldAndWindowInsetsDemo(modifier: Modifier = Modifier) {
    // 호출하는 곳에서 아래와 같이 innerPadding을 전달하면 window Insets와 더불어 2번 적용된다.
    // Modifier.padding(innerPadding)
    // 그래서 아래와 같이 consume을 호출해주는 것이 좋다.
//    Modifier
//        .padding(innerPadding)
//        .consumeWindowInsets(innerPadding)

    Column(
        modifier = modifier
            .statusBarsPadding()
            .background(Color.Red)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .imeNestedScroll()
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
        }
        // imePadding을 추가해주지 않으면 consumeWindowInsets 호출 시 키보드가 TextField를 가린다.
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.imePadding(),
        )
    }
}

@Preview
@Composable
private fun SpacingModifierDemoPreview() {
    ComposeStudyTheme {
        SpacingModifierDemo()
    }
}
