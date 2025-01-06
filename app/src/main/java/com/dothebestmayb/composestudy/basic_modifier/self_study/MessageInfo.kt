package com.dothebestmayb.composestudy.basic_modifier.self_study

import java.util.UUID

data class MessageInfo(
    val messageId: UUID = UUID.randomUUID(),
    val message: String,
    val replyTargetMessageInfo: MessageInfo? = null,
    val sender: MessageSender,
)

enum class MessageSender {
    ME, OTHER,
}
