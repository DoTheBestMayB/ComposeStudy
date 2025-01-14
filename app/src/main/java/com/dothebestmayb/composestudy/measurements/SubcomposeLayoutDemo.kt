package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMaxOfOrNull
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlin.random.Random

/**
 * Layout phase : measure children -> measure layout itself -> place children
 * Subcompose Layout phase : measure children -> subcompose -> measure layout itself -> place children
 *
 * subcompose : 조건에 따라 특정한 children을 measure 할 수 있음
 *
 * LazyList, BoxWithConstraint 모두 SubcomposeLayout을 사용함
 *
 * CustomLayout에 있는 PagedRow의 문제점 : 보이지 않은 child도 크기를 측정함
 */

@Composable
fun SubcomposePagedRow(
    page: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout (
        modifier = modifier,
    ) { constraints ->
        // SubcomposeMeasureScope에 있는 subcompose 함수를 활용해서 measure

        val pages = mutableListOf<List<Placeable>>()
        var currentPage = mutableListOf<Placeable>()
        var currentPageWidth = 0

        // slot은 Composable 한 개를 의미하는데, Scaffold에서 topBar, bottomBar 등도 slot 임
        // measureables는 measure 완료된 것이 아니라 measure 할 수 있는 것
        val measureables = subcompose("content", content)

        var i = 0
        for (measurable in measureables) {
            val placeable = measurable.measure(constraints)
            i++

            if (currentPageWidth + placeable.width > constraints.maxWidth) {
                if (pages.size == page) {
                    break
                }

                pages.add(currentPage)
                currentPage = mutableListOf()
                currentPageWidth = 0
            }
            currentPage.add(placeable)
            currentPageWidth += placeable.width
        }
        println("We measured $i composables")

        if (currentPage.isNotEmpty()) {
            pages.add(currentPage)
        }

        val pageItems = pages.getOrNull(page) ?: emptyList()
        val maxHeight = pageItems.fastMaxOfOrNull { it.height } ?: 0

        layout(constraints.maxWidth, maxHeight) {
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
private fun SubcomposeLayoutPreview() {
    ComposeStudyTheme {
        var page by remember {
            mutableIntStateOf(0)
        }
        Column {

            SubcomposePagedRow(
                page = page,
                modifier = Modifier
                    .background(Color.Red),
            ) {
                (1..1000).forEach {
                    Box(
                        modifier = Modifier
                            .width(Random.nextInt(300).dp)
                            .height(100.dp)
                            .background(Color(Random.nextInt()))
                    )
                }
            }
            Button(
                onClick = {
                    page++
                }
            ) {
                Text("Go to next page")
            }
        }
    }
}
