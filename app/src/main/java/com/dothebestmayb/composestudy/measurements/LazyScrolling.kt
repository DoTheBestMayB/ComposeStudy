package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme


/**
 * 아래 Composable을 실행하고 화면을 드래그할 때, LazyColumn이 보이는 순간 앱이 죽는다.
 * ```text
 * java.lang.IllegalStateException: Vertically scrollable component was measured with an infinity maximum height constraints, which is disallowed. One of the common reasons is nesting layouts like LazyColumn and Column(Modifier.verticalScroll()).
 * ```
 *
 * LazyColumn은 그려지는 Child의 크기를 알아야 한다.
 *
 * 그런데 Nested LazyColumn은 모든 Child가 화면에 보이는 것은 아니므로 모든 Text Composable의 height를 아는 것은 아니다.
 * 따라서 Nested LazyColumn의 Constraint maxHeight은 Infinity로 설정된다.
 *
 * 따라서 LazyColumn은 그려지는 Child의 크기를 알아야 한다는 사실에 위배되므로 Exception이 발생하는 것이다.
 */
@Composable
fun LazyScrolling(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(20) {
            Text(
                text = "Item $it",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        item {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(300.dp) // 높이를 설정하면 Infinity 문제를 해결할 수 있지만 적절한 해결책은 아니다.
            ) {
                items(10) {
                    Text(
                        text = "Item $it",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
        // Lazy 안에 같은 방향의 Lazy를 배치하고 싶으면 items 하위에 바로 배치하는 것이 좋다.
//        items(10) {
//            Text(
//                text = "Item $it",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//        }
    }
}

@Preview
@Composable
private fun LazyScrollingPreview() {
    ComposeStudyTheme {
        LazyScrolling()
    }
}
