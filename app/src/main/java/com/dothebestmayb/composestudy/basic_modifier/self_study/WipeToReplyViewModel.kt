package com.dothebestmayb.composestudy.basic_modifier.self_study

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class WipeToReplyViewModel: ViewModel() {

    private val _state = MutableStateFlow(
        WipeToReplyState(
            messages = listOf(
                MessageInfo(
                    messageId = UUID.randomUUID(),
                    message = "necessitatibus",
                    replyTargetMessageInfo = null,
                    sender = MessageSender.OTHER,
                )
            )
        )
    )
    val state = _state.asStateFlow()

    fun onAction(wipeToReplyAction: WipeToReplyAction) {
        when (wipeToReplyAction) {
            WipeToReplyAction.OnSendMessageClick -> {
                _state.update {
                    it.copy(
                        replyInfo = MessageInfo(
                            message = "",
                            sender = MessageSender.ME,
                        ),
                        isScrollToBottom = true,
                        messages = it.messages + it.replyInfo
                    )
                }
            }

            is WipeToReplyAction.OnReplyTextChanged -> {
                _state.update {
                    it.copy(
                        replyInfo = it.replyInfo.copy(
                            message = wipeToReplyAction.newText,
                        ),
                    )
                }
            }

            WipeToReplyAction.OnScrollBottomConsumed -> {
                _state.update {
                    it.copy(
                        isScrollToBottom = false,
                    )
                }
            }
        }
    }
}
