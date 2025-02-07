package com.dothebestmayb.composestudy.composition_locals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dothebestmayb.composestudy.measurements.negativePadding
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Button에 onColors를 설정하면, content Composable에 색상이 적용된다.
 * 이것은 CompositionLocals 때문인데, UI Component tree의 어디에서 Composition이 호출되었는지가 영향을 준다.
 *
 * Color 뿐만 아니라 TextStyle도 CompositionLocals이다.
 *
 */
@Composable
fun OverridingCompositionLocalDemo(modifier: Modifier = Modifier) {
    // 현재 Composable Scope에 적용되는 CompositionLocals를 확인하는 방법
    val contentColor = LocalContentColor.current
    val textStyle = LocalTextStyle.current

    // topAppBar는 Material3 디자인 시스템을 기본적으로 사용한다.
    // title은 topAppBar가 제공하는 TextStyle을 사용한다.
//    TopAppBar(
//        title = {
//            Text("Title")
//        }
//    )

    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Red
        )
    ) {


        // CompositionLocal을 override 하는 방법
        CompositionLocalProvider(
            LocalContentColor provides Color.Green
        ) {
            // Green이 적용됨
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
            Text("Hello World")
        }
        // Color.Red가 적용됨
//        Icon(
//            imageVector = Icons.Default.Check,
//            contentDescription = null,
////            tint = Color.Green, // override
//        )
//        Text("Hello World")
    }
}

/**
 * CompositionLocal을 이용해 slotAPI에 스타일을 적용하는 방법
 * 하지만 title Composable에서 설정한 스타일이 있다면, 해당 스타일이 적용됨
 */
@Composable
fun MyCustomAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
            )
        ) {
            title()
        }
    }
}

/**
 * Composable customModifier 사용을 지양해야 하는 이유와 관련된 예시
 *
 * negativePadding에서 LocalDensity를 이용한다.
 * 따라서 CompositionLocalProvider를 적용해도 내부에서 다시 정의하기 때문에 의도와 다르게 동작한다.
 *
 * Modifier Factory를 이용해 customModifier를 정의하면, CompositionLocalProvider에서 적용한 Density를 그대로 이어 받아 적용함
 */
@Composable
fun CustomModifierUsage(modifier: Modifier = Modifier) {
    val modifier = Modifier
        .negativePadding(16.dp)

    CompositionLocalProvider(LocalDensity provides TODO()) {
        Box(
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun OverridingCompositionLocalDemoPreview() {
    ComposeStudyTheme {
//        OverridingCompositionLocalDemo()
        MyCustomAppBar(
            title = {
                Text("Hello World")
            }
        )
    }
}
