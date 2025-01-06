package com.dothebestmayb.composestudy.basic_modifier.self_study

data class WipeToReplyState(
    val replyInfo: MessageInfo = MessageInfo(message = "", sender = MessageSender.ME),
    val isScrollToBottom: Boolean = false,
    val messages: List<MessageInfo> = emptyList(),
)
