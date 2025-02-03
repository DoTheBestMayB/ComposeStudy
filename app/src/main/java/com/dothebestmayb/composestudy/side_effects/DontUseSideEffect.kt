package com.dothebestmayb.composestudy.side_effects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * SideEffect는 최대한 사용하지 않는 것이 좋다.
 * State 상태에 따라 snackbar를 보여줘야 하는 경우도 SideEffect 없이 구현할 수 있다.
 *
 * LaunchedEffectDemo에 있는 코드를 LaunchedEffect 없이 구현해보자.
 */

class EffectHandlerViewModel : ViewModel() {

    private val _counter = MutableStateFlow(0)
    val counter = _counter.asStateFlow()

    val snackbarHostState = SnackbarHostState()

    val lazyListState = LazyListState()
    val canScrollToTop = snapshotFlow { lazyListState.firstVisibleItemIndex }
        .map { it >= 10 }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false,
        )

    init {
        viewModelScope.launch {
            counter.collectLatest { counter ->
                if (counter % 2 == 0) {
                    snackbarHostState.showSnackbar("The counter is even!")
                }
            }
        }
    }

    fun increaseCounter() {
        _counter.value++
    }

    /**
     * 모든 EffectHandler를 ViewModel에서 처리하는 것이 정답은 아니다.
     * lazyListState의 animateScrollToItem 함수는 Composable Coroutine Context에서 실행되어야 한다.
     * 이 코드가 실행되면 아래와 같은 에러가 발생한다.
     *
     * ```Text
     * java.lang.IllegalStateException: A MonotonicFrameClock is not available in this CoroutineContext. Callers should supply an appropriate MonotonicFrameClock using withContext.
     * ```
     *
     * 해결 방법 1 : Composable에서 rememberCoroutineScope을 통해 얻은 CoroutineScope 블록에서 실행하기
     *
     * 해결 방법 2 : Composable로부터 scope을 전달받아 viewModel에서 withContext로 전환하기
     * 이때, 전달받은 CoroutineScope을 ViewModel의 변수로 저장해서는 안 된다. viewModel이 Composable보다 lifecycle이 길기 때문에 메모리 누수가 발생할 수 있음
     */
    fun scrollTopTop(uiScope: CoroutineScope) {
        // Error
//        viewModelScope.launch {
//            lazyListState.animateScrollToItem(0)
//        }
        viewModelScope.launch {
            withContext(uiScope.coroutineContext) {
                lazyListState.animateScrollToItem(0)
            }
        }
    }
}

@Composable
fun DontUseSideEffectDemo(modifier: Modifier = Modifier) {
    val viewModel = viewModel<EffectHandlerViewModel>()
    val counter by viewModel.counter.collectAsStateWithLifecycle()
    val canScrollToTop by viewModel.canScrollToTop.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = viewModel.snackbarHostState
            )
        },
        floatingActionButton = {
            if (canScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        // Context 문제로 Exception 발생
//                        viewModel.scrollTopTop()

                        // 방법 1
//                        scope.launch {
//                            viewModel.lazyListState.animateScrollToItem(0)
//                        }
                        // 방법 2
                        viewModel.scrollTopTop(scope)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll To Top",
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = viewModel.lazyListState,
                modifier = Modifier
                    .weight(1f)
            ) {
                items(100) {
                    Text(
                        text = "Item $it",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
            Button(
                onClick = {
                    viewModel.increaseCounter()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Counter : $counter")
            }
        }
    }
}

@Preview
@Composable
private fun DontUseSideEffectDemoPreview() {
    ComposeStudyTheme {
        DontUseSideEffectDemo()
    }
}
