package com.dothebestmayb.composestudy.state_management.home_work

data class TodoState(
    val input: Todo = Todo(),
    val todoList: List<Todo> = emptyList(),
)
