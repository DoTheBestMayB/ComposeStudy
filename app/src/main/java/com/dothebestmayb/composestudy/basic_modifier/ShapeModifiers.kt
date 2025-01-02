package com.dothebestmayb.composestudy.basic_modifier

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.dothebestmayb.composestudy.R
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * RoundedCornerShape topStartPercent 파라미터를 이용해서 한 모서리만 라운딩 적용 가능
 */
@Composable
fun ShapeModifiersDemo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.living_room),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .background(Color.Red)
//            .clip(
//                RoundedCornerShape(
//                    percent = 50,
//                )
//            )
            .clip(TriangleShape)
            // clicp 이후에 적용하는 background는 효과가 없다.
            // 이미지가 파란색 background 위에 그려지기 때문?
            .background(Color.Blue)
    )
}

data object TriangleShape: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection, // ltr, rtl
        density: Density, // 화면 해상도
    ): Outline {
        // 팬을 이용해서 그리듯이 도형의 모양을 설정할 수 있다.
        return Outline.Generic(
            path = Path().apply {
                moveTo(
                    x = size.width / 2f,
                    y = 0f,
                )
                lineTo(
                    x = 0f,
                    y = size.height,
                )
                lineTo(
                    x = size.width,
                    y = size.height,
                )
                close()
            }
        )
    }
}


@Preview(
    showBackground = true,
)
@Composable
private fun ShapeModifiersDemoPreview() {
    ComposeStudyTheme {
        ShapeModifiersDemo()
    }
}
