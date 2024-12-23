@file:OptIn(ExperimentalLayoutApi::class)

package com.dothebestmayb.composestudy.basic_layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme


@Composable
fun FlowLayoutDemo(modifier: Modifier = Modifier) {
    var maxLines by remember {
        mutableIntStateOf(4)
    }

    FlowRow(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ), // 주로 주축에 해당하는 값만 설정한다. VerticalArrangment도 설정할 수 있다.
//        maxItemsInEachRow = 3, // 각 행에 포함될 아이템 개수의 최대값을 지정
        maxLines = maxLines, // 표시될 최대 줄
        // 최대 줄보다 더 많은 아이템이 있을 때 동작을 정의
        overflow = FlowRowOverflow.expandOrCollapseIndicator(
            expandIndicator = { // 펼쳐서 모두 보여주기
                IconButton(
                    onClick = {
                        maxLines += 2
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                }
            },
            collapseIndicator = { // 펼쳐진 아이템을 숨겨서 maxLines 만큼만 보여주기
                IconButton(
                    onClick = {
                        maxLines = 4
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                    )
                }
            },
        ),
    ) {
        for (i in 1..30) {
            AssistChip(
                onClick = {},
                label = {
                    Text("Item $i")
                }
            )
        }
    }
}

@Preview
@Composable
private fun FlowLayoutDemoPreview() {
    ComposeStudyTheme {
        FlowLayoutDemo()
    }
}