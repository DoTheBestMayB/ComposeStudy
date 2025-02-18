package com.dothebestmayb.composestudy.composition_locals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.basic_modifier.TriangleShape
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * CustomCompositionLocal을 사용하지 않고 구현할 수 있는 방법이 있다면, 그렇게 하는 것이 좋다.
 *
 * CompositionLocal은 전역 변수처럼 조심히 사용해야 한다.
 * Composable에서 CompositionLocal을 사용하는 것은 sideEffect라 할 수 있다.
 *
 * Custom CompositionLocal을 만들 때 주의할 점
 * 어디서 호출하더라도 해당 변수를 얻을 수 있어야 한다.
 * 예를 들어, LocalViewModel이라는 CompositionLocal을 만들었다고 하자.
 * ViewModel이 없는 Composition에서 LocalViewModel을 호출하면 null(또는 Exception)을 반환할 것이다.
 *
 * 또 다른 예시로, LocalNav Controller를 만든다면, Navigation Component를 사용하지 않는 Activity에서는 null(또는 Exception)을 반환할 것이다.
 */
@Composable
fun CustomCompositionLocalDemo(modifier: Modifier = Modifier) {
    /**
     * static Composition Local : 한 번 생성된 이후 변경되지 않는 Composition Local
     * ex) Context는 static이다. Activity가 생성된 이후, Activity 생명주기 동안 변경되지 않는다.
     * Activity가 파괴된 후 다시 생성될 때 Context가 다시 생성되지만, Activity가 파괴되면 Composable도 파괴되기 때문에 Composable 입장에서는 변하지 않는다.
     */
    LocalContext

    /**
     * static Composition Local이 변경되면, compositionLocal을 변경하기 위해
     * composition local provider를 호출한 Composable이 속한 ui tree가 강제로 recomposition 된다.
     */
//    CompositionLocalProvider(LocalContext provides TODO()) {
//
//    }

    // dynamic Composition Local : 변경 가능한 Composition Local
    LocalContentColor

}

// Custom dynamic CompositionLocal 만드는 방법
val LocalShape = compositionLocalOf {
    RectangleShape
}

@Composable
fun MyShapedButton(
    modifier: Modifier = Modifier
) {
    Column {
        Button(
            onClick = {},
            shape = LocalShape.current
        ) {
            Text("Before Changed!")
        }

        // LocalShape을 변경하는 방법
        CompositionLocalProvider(LocalShape provides TriangleShape) {
            Button(
                onClick = {},
                shape = LocalShape.current
            ) {
                Text("After Changed!")
            }
        }

        Button(
            onClick = {},
            shape = LocalShape.current
        ) {
            Text("Not Affected!")
        }
    }
}

@Preview
@Composable
private fun CustomCompositionLocalDemoPreview() {
    ComposeStudyTheme {
        MyShapedButton()
    }
}
