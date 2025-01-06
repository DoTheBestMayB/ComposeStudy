package com.dothebestmayb.composestudy.basic_modifier.self_study

sealed interface WipeToReplyAction {

    data class OnReplyTextChanged(val newText: String) : WipeToReplyAction
    data object OnSendMessageClick : WipeToReplyAction
    data object OnScrollBottomConsumed : WipeToReplyAction
}
