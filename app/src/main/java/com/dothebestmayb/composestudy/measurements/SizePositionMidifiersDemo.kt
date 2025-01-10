package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * onSizeChanged : Composable의 크기 변화를 감지하는 방법
 * onGloballyPositioned : Composable의 위치 변화를 감지하는 방법
 */
@Composable
fun SizePositionModifiersDemo(modifier: Modifier = Modifier) {
    var screenSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .onSizeChanged { newSize ->
                screenSize = newSize
            }
            .onGloballyPositioned { coordinates ->
                println("Position in window : ${coordinates.positionInWindow()}")
            }
    ) {
        Text(
            text = if (screenSize.width > 1500) {
                "Wide Screen!"
            } else {
                "Not so wide screen!"
            }
        )
    }
}

@Preview
@Composable
private fun SizePositionModifiersDemo() {
    ComposeStudyTheme {
        SizePositionModifiersDemo()
    }
}
