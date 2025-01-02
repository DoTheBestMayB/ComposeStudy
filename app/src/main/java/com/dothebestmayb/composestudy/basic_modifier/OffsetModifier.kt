package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * offset은 child layout measuring에 영향을 주지 않는다.
 * 실제 위치는 그대로 있고, 그려지는 위치만 변경하는 것이기 때문이다.
 *
 * padding은 같은 depth composable에 영향을 주지만, offset은 영향을 주지 않는다.
 *
 * offset의 이러한 특징은 animation 구현시 장점이 되는데, composable이 이동할 때마다
 * 각 composable의 위치를 다시 계산할 필요가 없어 성능상의 이점이 있다.
 */
@Composable
fun OffsetModifierDemo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .size(100.dp)
            .background(Color.Red)
            // child Composable이 시작할 지점을 조정함
//            .offset(
//                x = 50.dp,
//                y = 20.dp,
//            )
    ) {
        Text(
            text = "Hello World",
            modifier = Modifier
                // offset을 background 이후에 적용하면
                // 옮겨진 위치가 아닌, 옮겨지기 전 위치에 background가 적용됨
                .offset(
                    x = 50.dp,
                    y = 20.dp,
                )
                .background(Color.Green)
        )
        // padding은 같은 depth composable 위치에 영향을 준다.
//        Text(
//            text = "Hello World",
//            modifier = Modifier
//                .padding(
//                    start = 30.dp,
//                    top = 40.dp,
//                )
//                .background(Color.Cyan)
//        )
        Text(
            text = "Hello World",
            modifier = Modifier
                .background(Color.Yellow)
        )
    }

    // Offset lamdba 예시
    // 애니메이션 배울 때 더 자세하게 다룬다.
    Modifier
        .offset {
            // 람다의 수신 객체가 Density 이기 때문에 dp를 px로 변환 가능
            25.dp.roundToPx()
            IntOffset(50, 10)
        }
}

@Preview
@Composable
private fun OffsetModifierDemoPreview() {
    ComposeStudyTheme {
        OffsetModifierDemo()
    }
}
