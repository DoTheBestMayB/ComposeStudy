package com.dothebestmayb.composestudy.basic_modifier.self_study

import java.util.UUID

sealed interface WipeToReplyAction {

    data class OnReplyTextChanged(val newText: String) : WipeToReplyAction
    data class OnScroll(
        val messageId: UUID
    ) : WipeToReplyAction

    data class OnMessageSwiped(
        val messageInfo: MessageInfo
    ) : WipeToReplyAction

    data object OnSendMessageClick : WipeToReplyAction
    data object OnScrollConsumed : WipeToReplyAction
    data object OnClearReplyTarget : WipeToReplyAction
}
