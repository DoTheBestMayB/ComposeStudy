package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Composition pass : Composable이 그려지는 3단계 중 첫 번째로, 어떤 것을 그려야 할지 결정하는 단계
 * composable 간의 hierarchy를 설정함
 *
 * Measurement pass : Composable의 크기를 측정하는 단계
 *
 * Constraints 값에 따라 composable을 다르게 설정할 때 사용한다.
 */
@Composable
fun BoxWithConstraintsDemo(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = Modifier
            .width(200.dp)
    ) {
        if (constraints.hasFixedWidth) {
            Text("Fix width!")
        } else {
            Text("Dynamic width!")
        }
    }
}

@Preview
@Composable
private fun BoxWithConstraintsPreview() {
    ComposeStudyTheme {
        BoxWithConstraintsDemo()
    }
}
