package com.dothebestmayb.composestudy.state_management

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

private var count = 0
private var countState by mutableIntStateOf(0)

// remember block은 composable 내부에서만 사용할 수 있다.
// @Composable invocations can only happen from the context of a @Composable function
//private var countStateRemember by remember {
//    mutableIntStateOf(0)
//}

@Composable
fun Counter(modifier: Modifier = Modifier) {
    // recomposition이 발생할 때 변수도 다시 초기화 된다. -> side effect
    // remember로 wrapping하면 값을 캐싱한다. recomposition이 발생하면 캐싱된 값을 사용한다.
    var countStateRemember by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                // 아래처럼 구현하면 버튼을 눌러도 Count Text가 증가하지 않는다.
                // 버튼을 눌러도 recomposition이 발생하지 않기 때문이다.
//                count++

                countState++
            }
        ) {
            Text("Count: $countState")
        }
    }
}

/**
 *  버튼을 클릭하면 숫자가 계속 증가하며 UI가 다시 그려진다. 그 과정은 아래와 같다.
 *  1. 처음에 Text가 그려지기 전에 counter가 1 증가한다.
 *  2. 버튼을 누르면 counter 값이 변경되어 recomposition이 발생한다.
 *  3. counter를 사용하는 composable을 찾는다. -> Text
 *  4. Text가 포함된 composable block이 다시 그려진다.
 *  5. 이때 counter가 다시 1 증가한다.
 *  6. 다시 2번 수행(무한 반복...)
 *
 *  그래서 State는 non-composable 람다에서 변경해야 한다.
 *  onClick은 non-composable 람다임
 */
@Composable
fun CounterInfinityLoop(modifier: Modifier = Modifier) {
    // configuration change와 같은 이유로 Compose UI hierarchy가 다시 생성되면
    // remember에 의해 캐싱된 state도 초기화된다.
    // configuration change가 발생해도 값을 유지하고 싶으면 rememberSaveable을 이용해야 한다.
    var counter by rememberSaveable {
        mutableIntStateOf(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                counter++
            }
        ) {
            counter++
            Text("Count: $counter")
        }
    }
}

/**
 * 버튼을 클릭해도 UI가 변경되지 않는다.
 * State의 비교 방식 때문인데, recomposition이 발동하기 위한 조건은 state의 instance가 이전과 달라졌는지다.
 * 아래에서 아이템 목록을 가지고 있는 mutableListOf 인스턴스는 항상 동일하다.
 *
 * 결론 : Compose에서 사용하는 state는 immutable 해야 하며, state 자체를 새로운 인스턴스로 변경해서 recomposition을 유발하자.
 * mutableListOf, hashSetOf, var property를 포함한 data class를 지양하자
 */
@Composable
fun CommonMistake(modifier: Modifier = Modifier) {
    var items by rememberSaveable {
        mutableStateOf(
            mutableListOf<String>(
                "Item"
            )
        )
    }

    var itemsRight by rememberSaveable {
        mutableStateOf(
            listOf<String>()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                items.add("Item")
//                itemsRight += "Item"
            }
        ) {
            Text("Add item")
        }
        Text(
            text = items.toString(),
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
//        Text(
//            text = itemsRight.toString(),
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//        )
    }
}

@Preview
@Composable
private fun CommonMistakePreview() {
    ComposeStudyTheme {
        CommonMistake()
    }
}

@Preview
@Composable
private fun CounterInfinityLoopPreview() {
    ComposeStudyTheme {
        CounterInfinityLoop()
    }
}

@Preview
@Composable
private fun CounterPreview() {
    ComposeStudyTheme {

    }
}