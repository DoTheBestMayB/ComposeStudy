@file:OptIn(ExperimentalFoundationApi::class)

package com.dothebestmayb.composestudy.basic_layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun LazyListDemo(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // modifier padding margin처럼 적용
        // contentPadding은 진짜 padding처럼 적용
        contentPadding = PaddingValues(16.dp),
//        reverseLayout = true, // 아래에서부터 표시
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(100) { i ->
            Text("Item $i")
        }
    }
}

@Composable
fun StickyHeader(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(100) { i ->
            Text("Item $i")
        }
        // 아직 사용자에게 보이지 않으면 그려지지 않는 것은 동일하다.
        // 드래그하면 상단에 붙어서 계속 보여지게 된다.
        // 쓰임 예시 : 연락처에서 이름의 초성을 나타낼 때 쓸 수 있다. ㄱ, ㄴ, ㄷ, ...
        stickyHeader {
            Text(
                text = "STICKEY HEADER A",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
            )
        }
        items(100) { i ->
            Text("Item ${i + 100}")
        }
        // 이 composable이 상단에 위치하게 되면 이전 stickyHeader는 그려지지 않는다.
        stickyHeader {
            Text(
                text = "STICKEY HEADER B",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Green)
            )
        }
        items(100) { i ->
            Text("Item ${i + 200}")
        }
        // footer와 같은 UI가 필요할 때 사용할 수 있다.
        // item 또한 LazyLoading이 적용된다.
        item {
            Text(
                text = "Reached the end!",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            )
        }
    }
}


@Preview(
    showBackground = true,
)
@Composable
private fun LazyListDemoPreview() {
    ComposeStudyTheme {
        LazyListDemo()
    }
}