package com.dothebestmayb.composestudy.side_effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Compose State를 다시 Flow로 변환하려면 어떻게 해야 할까?
 * 이때 사용하는 것이 snapshotFlow
 */
class MyViewModel : ViewModel() {

    // stateFlow가 compose state보다 더 많은 기능(map, filter)을 제공해서 philip은 stateflow를 선호한다.
//    private val _state = MutableStateFlow(0)
//    val state = _state.asStateFlow()

    // 그런데 compose state를 사용하는 경우를 가정해보자.
    var state by mutableIntStateOf(0)
        private set

    // snapshotFlow를 이용해서 compose state를 stateFlow로 변환할 수 있다.
    private val myStateAsFlow = snapshotFlow {
        state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        0
    )
}


@Composable
fun MyComposable(modifier: Modifier = Modifier) {
    val viewModel = viewModel<MyViewModel>()
//    val state by viewModel.state.collectAsStateWithLifecycle()
}
