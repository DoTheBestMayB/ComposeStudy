package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * focusRequester를 이용해서 특정 composable에 focus를 요청할 수 있다.
 * Modifier.focusRequester에 focusRequester를 할당하고, focusRequester.requestFocus() 함수 호출
 *
 * focusManager를 이용해서 focus를 옮길 수 있다.
 * focus를 갖을 수 없는 composable은 건너뛴다.
 * clearFocus() 를 이용해 포커스를 없앨 수 있다.
 *
 * keyboardOptions를 이용해 focus를 가졌을 때 가상 키보드의 IME action을 수정할 수 있다.
 */
@Composable
fun FocusManagementModifiersDemo(modifier: Modifier = Modifier) {
    val focusRequester = remember {
        FocusRequester()
    }
    // focus를 옮길 때 사용하면 좋다.
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .focusRequester(focusRequester),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        var isFocused by remember {
            mutableStateOf(false)
        }
        // Box는 기본적으로 focus를 갖지 않는다. 따라서 위 TextField에서 imeaction을 누르면
        // Box가 아닌 그 다음 TextField로 간다.
        // Box도 focus를 갖을 수 있도록 Modifier의 focusable로 설정해주면 된다.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(
                    width = 5.dp,
                    color = if (isFocused) Color.Red else Color.Gray
                )
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusable()
        )
        TextField(
            value = "",
            onValueChange = {},
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        var isColumnFocused by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 5.dp,
                    color = if (isColumnFocused) Color.Red else Color.Gray
                )
                .onFocusChanged {
                    isColumnFocused = it.hasFocus
                }
                .focusGroup()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TextField(
                value = "",
                onValueChange = {},
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                )
            )
            TextField(
                value = "",
                onValueChange = {},
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                )
            )
        }
        TextField(
            value = "",
            onValueChange = {},
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        Button(
            onClick = {
                focusRequester.requestFocus()
            }
        ) {
            Text("Start filling out form")
        }
        // focus 없애기
        Button(
            onClick = {
                focusManager.clearFocus()
            }
        ) {
            Text("Clear focus")
        }
    }
}

@Preview
@Composable
private fun FocusManagementModifiersPreview() {
    ComposeStudyTheme {
        FocusManagementModifiersDemo()
    }
}
