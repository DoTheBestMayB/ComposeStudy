package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.R
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * item을 삭제하거나 추가하면 Compose는 myList를 새로운 리스트라고 판단해서 recomposition을 발생시킨다.
 * 이것을 방지(recomposition skip)하기 위해 LazyList items에 key를 지정해줘야 한다.
 */
@Composable
fun LazyListPerformance(modifier: Modifier = Modifier) {
    var myList by remember {
        mutableStateOf(
            (1..100).map {
                MyListItem(
                    id = it,
                    title = "List item $it",
                    description = "Description $it"
                )
            }
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                testTagsAsResourceId = true
            },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val highestVal = myList.maxOf { it.id }
                    myList = listOf(
                        MyListItem(
                            id = highestVal + 1,
                            title = "List item ${highestVal + 1}",
                            description = "Description ${highestVal + 1}",
                        )
                    ) + myList
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = "Add item"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("main_list")
        ) {
            items(
                items = myList,
                key = { it.id }, // key를 설정해줘야 아이템을 삭제하거나 추가할 때 recomposition이 최적화(skip)된다.
            ) { item ->
                ListItem(
                    headlineContent = {
                        Text(item.title)
                    },
                    supportingContent = {
                        Text(item.description)
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                myList -= item
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_delete_24),
                                tint = Color.Red,
                                contentDescription = "Delete"
                            )
                        }
                    }
                )
            }
        }
    }
}

data class MyListItem(
    val id: Int,
    val title: String,
    val description: String
)

@Preview(
    showBackground = true
)
@Composable
private fun LazyListPerformancePreview() {
    ComposeStudyTheme {
        LazyListPerformance()
    }
}
