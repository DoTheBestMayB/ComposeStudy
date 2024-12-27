package com.dothebestmayb.composestudy.state_management.number_guess

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/**
 * philip은 StateFlow를 선호한다. Activity에서도 사용할 수 있고, 상태를 저장하는 것 외 다양한 기능을 Flow가 제공하기 때문이라고 한다.
 * ex) update 함수를 이용하면 thread safe하게 state를 갱신할 수 있다.
 */
class NumberGuessViewModel : ViewModel() {

    private val random = Random(System.currentTimeMillis())

    // number를 NumberGuessState에 포함하지 않는 이유는, random 숫자를 UI에 표시하지 않기 때문이다.
    // State는 맞지만 Composable State는 아니다.
    private var number = random.nextInt(1, 101)
    private var attempts = 0

    // state를 이용하는 방법
//    var state by mutableStateOf(NumberGuessState())
//        private set

    // steteFlow를 이용하는 방법
    private val _state = MutableStateFlow(NumberGuessState())
    val state = _state.asStateFlow()

    fun onAction(action: NumberGuessAction) {
        when (action) {
            NumberGuessAction.OnGuessClick -> {
                val guess = state.value.numberText.toIntOrNull()
                if (guess != null) {
                    attempts++
                }

                _state.update {
                    it.copy(
                        guessText = when {
                            guess == null -> "Please enter a number."
                            number > guess -> "Nope, my number is larger."
                            number < guess -> "Nope, my number is smaller."
                            else -> "That was it! You needed $attempts attempts."
                        },
                        isGuessCorrect = guess == number,
                        numberText = ""
                    )
                }
            }

            is NumberGuessAction.OnNumberTextChanged -> {
                _state.update {
                    it.copy(numberText = action.numberText)
                }
            }

            NumberGuessAction.OnStartNewGameButtonClick -> {
                // 아래와 같이 value로 변경할 수도 있지만, update를 사용하면 thread safe하다.
//                _state.value = _state.value.copy()
                number = random.nextInt(1, 101)
                _state.update {
                    it.copy(
                        numberText = "",
                        guessText = null,
                        isGuessCorrect = false,
                    )
                }
            }
        }
    }
}