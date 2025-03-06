package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 *
 * targetValue가 변경되면 변경된 값을 가지고 Animation이 동작한다.
 * label은 animation preview에서 animation을 구분하는 값으로 사용된다.
 */
@Composable
fun AnimateStateAsDemo(modifier: Modifier = Modifier) {
    var toggle by remember {
        mutableStateOf(false)
    }
    val ratio by animateFloatAsState(
        targetValue = if (toggle) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ratio animation"
    )
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = modifier
                    // block은 canvas
                    // animation은 draw phase에 동작한다.
                    // animation에 의한 크기 변화는 measure phase가 아니다?
                    .graphicsLayer {
                        rotationZ = 405f * ratio
                        scaleX = 1f - (ratio * 0.5f)
                        scaleY = 1f - (ratio * 0.5f)
                    }
                    .size(100.dp)
                    .background(Color.Red)
            )
        }
        Button(
            onClick = {
                toggle = !toggle
            }
        ) {
            Text("Toggle")
        }
    }
}

@Preview
@Composable
private fun AnimateStateAsDemoPreview() {
    ComposeStudyTheme {
        AnimateStateAsDemo()
    }
}
