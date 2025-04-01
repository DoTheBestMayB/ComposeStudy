package com.dothebestmayb.composestudy.animations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * LazyList에 사용되는 Item의 Modifier로 `animateItem`을 설정하면
 * 아이템의 순서가 변경될 때 애니메이션이 적용된다.
 * 또한, 아이템이 삭제될 때 fadeout, 아이템이 추가될 때 fadein 애니메이션이 적용된다.
 */
@Composable
private fun AnimateListChangesDemo(modifier: Modifier = Modifier) {
    var items by remember {
        mutableStateOf(
            (1..100).map {
                "List Item $it"
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                items = items.shuffled()
            }
        ) {
            Text("Shuffle")
        }
        Button(
            onClick = {
                items = items.take(1) + "List Item ${items.size + 1}" + items.takeLast(items.size - 1)
            }
        ) {
            Text("Add")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                items = items,
                key = { it },
            ) { text ->
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            items = items - text
                        }
                        .padding(16.dp)
                        .animateItem()
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnimateListChangesDemoPreview() {
    ComposeStudyTheme {
        AnimateListChangesDemo()
    }
}
