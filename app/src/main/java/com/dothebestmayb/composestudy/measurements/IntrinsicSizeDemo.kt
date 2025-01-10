package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Q. 위에 Row CheckBox가 아래 Row CheckBox와 정렬되도록 하려면 어떻게 해야 할까?
 * A. IntrinsicSize를 활용한다.
 */
@Composable
fun IntrinsicSizeDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            // child 중 크기가 가장 큰 composable의 width로 길이를 설정함
            .width(IntrinsicSize.Max),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Option 1"
            )
            Checkbox(
                checked = true,
                onCheckedChange = {}
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Option 1, but in longer"
            )
            Checkbox(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

@Composable
fun IntrinsicSizeDemo2(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            // child 중 크기가 가장 큰 composable의 width로 길이를 설정함
            .width(IntrinsicSize.Max),
    ) {
        Text(
            text = "Hello world I am some kind of longer text",
            // Parent의 maxWidth가 text 크기로 설정됐기 때문에 fillMaxWidth는 효과가 없다.
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IntrinsicSizeDemo3(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            // 가장 짧은 길이 child인 Text에서 가장 긴 단어를 maxWidth로 설정함
            .width(IntrinsicSize.Min),
    ) {
        Text(
            text = "Hello@@@@@@@@@@@ world I am some kind of longer text",
            // Parent의 maxWidth가 text 크기로 설정됐기 때문에 fillMaxWidth는 효과가 없다.
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun IntrinsicSizeDemoPreview() {
    ComposeStudyTheme {
//        IntrinsicSizeDemo()
//        IntrinsicSizeDemo2()
        IntrinsicSizeDemo3()
    }
}
