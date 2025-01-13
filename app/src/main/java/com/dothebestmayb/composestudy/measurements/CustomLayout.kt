package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMaxOfOrNull
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Layout이 차지하는 width 이하인 child Composable 들을 묶어서 한 개의 페이지로 설정하고
 * 한 번에 한 개의 페이지만 보여주는 Layout임
 *
 * 모든 Composable은 Layout에 기초한다.
 *
 * fastForEach는 iterate 과정에서 iterator를 생성하는 오버헤드를 피하기 위해 도입됐다.
 * Array, ArrayList와 같이 인덱스를 통한 빠른 접근이 가능한 컬렉션에 효율적이다.
 * LinkedList와 같이 인덱스 접근이 비효율적인 자료구조에 사용하면 오히려 성능이 저하될 수 있다.
 *
 * https://stackoverflow.com/questions/78725398/when-to-use-fastforeach-vs-foreach
 * https://discuss.kotlinlang.org/t/for-statement-performance-improvement-kotlin-jvm/22805/21
 *
 * iterator를 생성하는 것이 오버헤드인 이유는
 * 1. 각 iterator는 새로운 객체를 생성한다.
 * 2. 반복적으로 iterator를 생성하면 불필요한 객체가 늘어나 GC의 부하를 증가시킨다.
 * 3. iterator의 hasNext와 next 함수 호출은 추가적인 실행 비용을 유발한다.
 */
@Composable
fun PagedRow(
    page: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Measure의 순서
        // 1. child 측정
        // 2. 자신 측정
        // 3. child 배치하기

        val placeables = measurables.map {
            // 각 child가 얼마나 크기를 차지하는지 설정
            // The incoming minimum constraints in the video should be reset to 0. Otherwise, the children will be forced to stick to the parent constraints which can force them to fill the whole width when fillMaxWidth() is used.
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val pages = mutableListOf<List<Placeable>>()
        var currentPage = mutableListOf<Placeable>()
        var currentPageWidth = 0

        placeables.fastForEach { placeable ->
            if (currentPageWidth + placeable.width > constraints.maxWidth) {
                pages.add(currentPage)
                currentPage = mutableListOf()
                currentPageWidth = 0
            }
            currentPage.add(placeable)
            currentPageWidth += placeable.width
        }

        if (currentPage.isNotEmpty()) {
            pages.add(currentPage)
        }

        val pageItems = pages.getOrNull(page) ?: emptyList()

        layout(constraints.maxWidth, constraints.maxHeight) {
            var xOffset = 0
            pageItems.fastForEach { placeable ->
                placeable.place(xOffset, 0)
                xOffset += placeable.width
            }
        }
    }
}

@Preview
@Composable
private fun CustomLayoutPreview() {
    ComposeStudyTheme {
        PagedRow(
            page = 0,
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(150.dp)
                    .background(Color.Yellow)
            )
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(100.dp)
                    .background(Color.Green)
            )
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .background(Color.Blue)
            )
        }
    }
}
