package com.dothebestmayb.composestudy.composition_locals

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * Composition Local : Global variable specific to composition(UI Tree)
 * CompositionLocal is a tool for passing data down through the Composition implicitly
 *
 * LocalConfiguration : 기기의 설정(언어, 화면 방향 등)을 알 수 있음
 * LocalContext : Context가 필요할 때, Activity에서 필요한 Composable까지 파라미터로 넘겨줄 필요 없이 이 변수 사용하면 됨
 * LocalClipboardManager : 복사, 붙여넣기 관련
 * LocalSoftwareKeyboardController : 가상키보드를 보여주거나 숨길 수 있음
 */
@Composable
fun CompositionLocalDemo(modifier: Modifier = Modifier) {
    LocalSoftwareKeyboardController
}
