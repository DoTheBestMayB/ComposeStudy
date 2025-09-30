package com.dothebestmayb.composestudy.performance_optimization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

data class Section(
    val id: Int,
    val header: String,
    val description: String,
)

/**
 * shuffle 될 때마다 모든 Section이 recomposition된다.
 * LazyList에 key를 전달해 recomposition을 방지한 것처럼
 * Key composable을 이용해 모든 Layout(Column, Row, FlowRow 등)에 동일한 효과를 적용할 수 있다.
 */
@Composable
fun KeysCustomLayout(modifier: Modifier = Modifier) {
    var sections by remember {
        mutableStateOf(
            (1..3).map {
                Section(
                    id = it,
                    header = "Section $it Header",
                    description = "Section $it description"
                )
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for (section in sections) {
            // 핵심
            key(section.id) {
                Section(section)
            }
        }
        Button(
            onClick = {
                sections = sections.shuffled()
            }
        ) {
            Text("Shuffle")
        }
    }
}

@Composable
fun Section(
    section: Section,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = section.header,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = section.description,
        )
    }
}

@Composable
fun KeysCustomLayoutPreview(modifier: Modifier = Modifier) {
    ComposeStudyTheme {
        KeysCustomLayout()
    }
}
