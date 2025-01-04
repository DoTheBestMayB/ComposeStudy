package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun ClickableModifiersDemo(modifier: Modifier = Modifier) {
    // click effect를 관찰할 수 있도록 해준다. ex) 누른다, 누른걸 푼다 등
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    // s pen을 이용해서 하버링 테스트 가능
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .size(100.dp)
            .background(
                if (isPressed) Color.Blue else Color.White
//                if (isHovered) Color.Blue else Color.White
            )
            // padding이 clickable보다 먼저 위치하기 때문에, ripple effect는 전체가 아닌 padding 내부 영역만 적용됨
            .padding(16.dp)
            .clickable(
                enabled = true, // 클릭 가능 여부를 설정할 수 있음
                onClickLabel = null, // accessibility를 위한 값
                interactionSource = interactionSource,
                // ripple effect를 위한 설정(눌렀을 때 클릭한 지점 중심으로 동그라미 퍼지는 효과)
                indication = LocalIndication.current,
            ) {
                println("Hello World!")
            },
        contentAlignment = Alignment.Center,
    ) {
        Text("Hello World!")
    }
}

@Preview
@Composable
private fun ClickableModifiersDemoPreview() {
    ComposeStudyTheme {
        ClickableModifiersDemo()
    }
}
