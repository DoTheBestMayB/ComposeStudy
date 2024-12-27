package com.dothebestmayb.composestudy.state_management.number_guess

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme


/**
 * viewModel 생성자에 주입되는 객체가 있을 때, composable 내부에서 viewModel을 생성하면 preview를 사용할 수 없다.
 * preview는 해당 객체를 어떻게 주입하는지 모른다.
 * 그래서 ViewModel을 생성하는 Root composable을 별도로 만든다.
 *
 * 현재는 생성자에 주입되는 객체가 없어서 기본 viewModel 함수를 사용했지만, 주입되는 객체가 있으면
 * Hilt, Coin과 같은 DI 라이브러리를 사용해야 한다.
 *
 */
@Composable
fun NumberGuessScreenRoot(modifier: Modifier = Modifier) {
    // viewModel : https://developer.android.com/develop/ui/compose/libraries#viewmodel
    val viewModel = viewModel<NumberGuessViewModel>()
    // 아래 함수를 사용하려면 의존성 추가 필요함 : "androidx.lifecycle:lifecycle-runtime-ktx"
    // collectAsState 함수가 있는 이유 : Android에 대한 의존성이 없는 멀티 플랫폼에서 사용하기 위한 함수임
    val state by viewModel.state.collectAsStateWithLifecycle()

    NumberGuessScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

/**
 * MVI 패턴을 사용함으로써 Composable과 ViewModel 간의 의존성이 끊어졌다.
 *
 * TextField에 값을 변경하면 viewModel에서 copy 함수를 통해 state를 갱신한다.
 * 이때, 단순히 numberText만 변경됨에도 불구하고, state가 갱신되었기 때문에,
 * state를 사용하는 Composable은 모두 recomposition 될 것 같다.
 *
 * 실제로는 그렇지 않다. TextField에 해당하는 property가 변경되면 Compose는 smart하게 TextField만 Recomposition 한다.
 */
@Composable
fun NumberGuessScreen(
    state: NumberGuessState,
    onAction: (NumberGuessAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        TextField(
            value = state.numberText,
            onValueChange = { newText ->
                onAction(NumberGuessAction.OnNumberTextChanged(newText))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Button(
            onClick = {
                onAction(NumberGuessAction.OnGuessClick)
            }
        ) {
            Text("Make guess")
        }
        if (state.guessText != null) {
            Text(
                text = state.guessText
            )
        }
        if (state.isGuessCorrect) {
            Button(
                onClick = {
                    onAction(NumberGuessAction.OnStartNewGameButtonClick)
                }
            ) {
                Text(
                    text = "Start New Game"
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun NumberGuessScreenPreview() {
    ComposeStudyTheme {
        NumberGuessScreen(
            state = NumberGuessState(
                numberText = "1234"
            ),
            onAction = {},
        )
    }
}