package com.dothebestmayb.composestudy.state_management.home_work

import java.util.UUID

data class Todo(
    val key: UUID = UUID.randomUUID(),
    val title: String = "",
    val description: String = "",
    val isChecked: Boolean = false,
)
