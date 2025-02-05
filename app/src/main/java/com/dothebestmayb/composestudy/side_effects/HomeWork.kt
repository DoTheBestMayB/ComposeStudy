package com.dothebestmayb.composestudy.side_effects

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

private val rand = Random(System.currentTimeMillis())

@Composable
fun SideEffectFreeComposableRoot(modifier: Modifier = Modifier) {
    val snackbarState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarState)
        }
    ) { paddingValues ->
        SideEffectFreeComposable(
            strings = List(100) {
                rand.nextInt().toString()
            },
            onBottomReached = {
                scope.launch {
                    snackbarState.showSnackbar("Scrolled to the bottom!")
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        )
    }
}

@Composable
fun SideEffectFreeComposable(
    strings: List<String>,
    onBottomReached: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    if (!lazyListState.canScrollForward) {
        onBottomReached()
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        items(strings) {
            Text(
                text = it
            )
        }
    }
}

internal class ScrollToBottomViewModel: ViewModel() {

    val snackbarHostState = SnackbarHostState()
    val lazyListState = LazyListState()

    init {
        snapshotFlow {
            val layoutInfo = lazyListState.layoutInfo
            layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
        }.onEach { isScrolledToEnd ->
            if (isScrolledToEnd) {
                snackbarHostState.showSnackbar("Scrolled to the bottom!")
            }
        }.launchIn(viewModelScope)
    }
}

@Composable
private fun AnswerRoot(modifier: Modifier = Modifier) {
    val viewModel = viewModel<ScrollToBottomViewModel>()

    Answer(
        snackbarHostState = viewModel.snackbarHostState,
        lazyListState = viewModel.lazyListState,
        strings = List(30) {
            "Item $it"
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun Answer(
    snackbarHostState: SnackbarHostState,
    lazyListState: LazyListState,
    strings: List<String>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(strings) {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun SideEffectFreeComposablePreview() {
    ComposeStudyTheme {
//        SideEffectFreeComposableRoot()
        AnswerRoot()
    }
}
