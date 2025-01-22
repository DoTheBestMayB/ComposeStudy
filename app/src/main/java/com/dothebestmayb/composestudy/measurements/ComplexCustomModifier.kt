package com.dothebestmayb.composestudy.measurements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun Modifier.negativePadding(horizontal: Dp): Modifier {
    val density = LocalDensity.current
    val px = with(density) {
        horizontal.roundToPx()
    }

    return layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints = constraints.copy(
                // 양쪽에 padding을 적용하므로 2를 곱한 값을 더함
                minWidth = constraints.minWidth + 2 * px,
                maxWidth = constraints.maxWidth + 2 * px,
            )
        )

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Composable
fun MyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "This is item 1",
            modifier = Modifier
                .clickable {

                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .negativePadding(16.dp)
        )
        Text(
            text = "This is another item",
            modifier = Modifier
                .clickable {

                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .negativePadding(16.dp)
        )
        Text(
            text = "Add another one",
            modifier = Modifier
                .clickable {

                }
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                onClick = {}
            ) {
                Text("Button 1")
            }
            Button(
                onClick = {}
            ) {
                Text("Button 2")
            }
            Button(
                onClick = {}
            ) {
                Text("Button 3")
            }
        }
    }
}

@Preview
@Composable
private fun MyListPreview() {
    ComposeStudyTheme {
        MyList()
    }
}
