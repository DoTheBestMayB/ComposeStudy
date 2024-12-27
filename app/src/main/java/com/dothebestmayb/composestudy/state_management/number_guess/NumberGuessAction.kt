package com.dothebestmayb.composestudy.state_management.number_guess

sealed interface NumberGuessAction {
    data object OnGuessClick: NumberGuessAction
    data class OnNumberTextChanged(val numberText: String): NumberGuessAction
    data object OnStartNewGameButtonClick: NumberGuessAction
}