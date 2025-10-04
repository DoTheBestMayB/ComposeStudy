package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Android Rendering system은 layer 단위로 겹겹이 그린다.(drawing works layer by layer)
 *
 * 1. 기본 배경 그린다.
 * 2. 그 위에 흰 배경의 Column 그린다.
 * 3. 그 위에 Box를 그린다.
 *
 * 그래서 겹쳐서 보이지 않는 부분도 안 그리는 것이 아니라 동일 픽셀 위에 덧대서 그리는 개념이다.
 * 이것을 overdraw라고 부른다.
 *
 * 개발자 옵션 -> Debug overdraw 설정을 통해 겹쳐서 그려지는 부분을 확인할 수 있다.
 * 파란색 : overdraw 되지 않음
 * 초록색 : overdraw 됨
 * 빨간색 : 많이 overdraw 됨
 */
@Composable
fun OverDrawDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Green)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OverDrawDemoPreview() {
    ComposeStudyTheme {
        OverDrawDemo()
    }
}
