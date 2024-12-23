package com.dothebestmayb.composestudy.basic_layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlin.random.Random
import kotlin.random.nextInt


/**
 * LazyGrid와의 차이는 각 아이템이 다양한 Height를 가질 수 있다.
 */
@Composable
fun LazyStaggeredGridDemo(modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        items(100) {
            Box(
                modifier = Modifier
                    .height(
                        height = Random.nextInt(1..200).dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(Random.nextInt()))
            )
        }
    }
}

/**
 * LazyGrid에서 width 속성은 일반적으로 무시된다.
 * Box에서 modifier.size를 이용해 설정하더라도, columns 설정에 따라 width가 override됨
 *
 * GridCells.FixedSize : 설정한 크기만큼 width를 가지도록 설정. 화면 크기를 벗어날 경우
 * FlowLayout처럼 아래로 내림
 */
@Composable
fun LazyGridDemo(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(100) {
            Box(
                modifier = Modifier
                    .width(
                        width = Random.nextInt(1..200).dp
                    )
                    .height(100.dp)
                    .background(Color(Random.nextInt()))
            )
        }
    }
}

@Preview
@Composable
private fun LazyStaggeredGridPreview() {
    ComposeStudyTheme {
        LazyStaggeredGridDemo()
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun LazyGridDemoPreview() {
    ComposeStudyTheme {
        LazyGridDemo()
    }
}