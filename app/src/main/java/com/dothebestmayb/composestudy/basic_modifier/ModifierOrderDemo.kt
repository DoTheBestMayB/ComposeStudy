package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * modifier는 모든 Composable에 적용 가능한 공통된 속성을 적용할 수 있는 파라미터다.
 */
@Composable
fun BasicModifierDemo(modifier: Modifier = Modifier) {
    // padding은 이후에 나오는 modifier에 적용되기 때문에
    // background 색상이 박스 전체가 아닌 padding 내부 영역에만 적용됐다.
    // clip이 Text에 적용된 이유도 순서상 뒤에 나왔기 때문이다.
//    Box(
//        modifier = Modifier
//            .size(100.dp)
//            .padding(16.dp)
//            .background(Color.Red)
//            .clip(CircleShape)
//        ,
//        contentAlignment = Alignment.Center,
//    ) {
//        Text("Hello World!")
//    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Red)
            .padding(16.dp)
            .clip(CircleShape)
            .background(Color.Green),
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello World!")
    }
}

@Preview
@Composable
private fun ModifierOrderDemo() {
    ComposeStudyTheme {
        BasicModifierDemo()
    }
}
