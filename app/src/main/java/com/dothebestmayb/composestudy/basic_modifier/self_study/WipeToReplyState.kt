package com.dothebestmayb.composestudy.basic_modifier.self_study

data class WipeToReplyState(
    val replyInfo: MessageInfo = MessageInfo(message = "", sender = MessageSender.ME),
    val scrollDestination: Int? = null,
    val messages: List<MessageInfo> = emptyList(),
)
