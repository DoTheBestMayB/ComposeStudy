package com.dothebestmayb.composestudy.side_effects

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.launch

/**
 * 아래로 아이템 10개 크기 이상 스크롤한 경우에만 Floating button 보여주기
 *
 * LaunchedEffect를 이용해서 구현은 할 수 있으나, 성능상 좋지 않다.
 * 상태가 자주 변경 되는 state를 LaunchedEffect의 key로 전달하는 것은 부적절함
 *
 * 이러한 상황에서는 derivedStateOf를 사용할 수 있다.
 * input state는 자주 변경되는 반면, output state는 자주 변경되지 않는 구조에 적합하다.
 * LazyListState는 매우 자주 변경되는 반면, 리스트에서 처음 보이는 아이템의 Index가 10 이상인가?는 true 혹은 false로 자주 변경되지 않음
 *
 * 정리
 * 1. state의 변화보다 결괏값의 변화가 적다 : derivedStateOf 사용
 * 2. state의 변화와 결과의 변화가 동일하다 : remember + key 사용
 * 3. state의 변화보다 결괏값의 변화가 적은 것과 동일한 것을 동시에 사용한다 : remember + key + derivedStateOf 사용
 */
@Composable
fun DerivedStateOfDemo(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()

    /**
     * 1. LaunchedEffect를 이용한 방법 - 적절하지 않음
     */
//    var showScrollToTopButton by remember {
//        mutableStateOf(false)
//    }
//    LaunchedEffect(state.firstVisibleItemIndex) {
//        showScrollToTopButton = state.firstVisibleItemIndex >= 10
//    }

    /**
     * 2. remember with key를 이용한 방법 - 적절하지 않음
     * https://www.youtube.com/watch?v=_bb0PVBe3eQ
     *
     * 원하는 기능은 구현할 수 있으나, key가 변경될 때마다 state가 갱신되고
     * state를 if문으로 사용하고 있는 FloatingActionButton은 매번 Recomposition 된다.
     *
     * 왜 recomposition 되는가?
     * 1. Remember는 composable function이다.
     * 2. 파라미터인 key로 입력된 compose state가 변경되었다.
     * 3. Compose Runtime은 Recomposition을 실행
     *
     * 그리고 State가 아닌 Boolean value이다.
     */
//    val showScrollToTopButton = remember(state.firstVisibleItemIndex) {
//        state.firstVisibleItemIndex >= 10
//    }

    /**
     * 3. derivedStateOf
     *
     * derivedStateOf는 Compose State임
     * derivedStateOf 람다 블록 결괏값이 변경되는 경우에만 state가 갱신됨
     */
/*    val showScrollToTopButton by remember {
        derivedStateOf {
            state.firstVisibleItemIndex >= 10
        }
    }*/

    /**
     * derivedStateOf와 remember + key의 recomposition 횟수가 동일한 경우 무엇을 써야 할까?
     * remember + key를 사용하는 것이 적합하다. -> key가 변경될 때 state가 변경되는 것이 remember + key의 의도에 맞다고 philip은 설명함
     *
     * 그렇다면 remember + key + derivedStateOf의 조합은 언제 사용할까?
     * 추가적으로 floatingActionButton을 보여줄 지를 결정하는 boolean 조건을 추가해보자.
     *
     * showScrollToTopButton가 true인 상황에서, isEnabled만 false로 변경되면 어떻게 될까?
     * 여전히 showScrollToTopButton는 true이다.
     *
     * 이때, remember의 key로 isEnabled를 추가하면, isEnabled가 변경됐을 때도 recomposition 된다.
     */

    var isEnabled by remember {
        mutableStateOf(true)
    }
    // key로 isEnabled를 추가하지 않으면 isEnabled가 false로 변경돼도, 즉시 showScrollToTopButton에 반영되지 않는다.
    val showScrollToTopButton by remember(isEnabled) {
        derivedStateOf {
            state.firstVisibleItemIndex >= 10 && isEnabled
        }
    }

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(100) {
            Text(
                text = "Item $it",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
    val scope = rememberCoroutineScope()

    if (showScrollToTopButton) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    state.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    end = 16.dp,
                    bottom = 16.dp
                )
                .wrapContentSize(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
private fun DerivedStateOfDemoPreview() {
    ComposeStudyTheme {
        DerivedStateOfDemo()
    }
}
