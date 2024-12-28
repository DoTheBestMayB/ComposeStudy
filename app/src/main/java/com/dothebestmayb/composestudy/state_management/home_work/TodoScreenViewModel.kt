package com.dothebestmayb.composestudy.state_management.home_work

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoScreenViewModel : ViewModel() {

    private val _state = MutableStateFlow(TodoState())
    val state = _state.asStateFlow()

    fun onAction(action: TodoAction) {
        when (action) {
            TodoAction.OnAddClick -> {
                _state.update {
                    it.copy(
                        input = Todo(),
                        todoList = it.todoList + it.input,
                    )
                }
            }

            is TodoAction.OnCheckClick -> {
                _state.update {
                    it.copy(
                        todoList = it.todoList.map { todoItem ->
                            if (todoItem == action.todo) {
                                todoItem.copy(isChecked = !todoItem.isChecked)
                            } else {
                                todoItem
                            }
                        }
                    )
                }
            }

            is TodoAction.OnDescriptionChanged -> {
                _state.update {
                    it.copy(
                        input = it.input.copy(
                            description = action.description,
                        )
                    )
                }
            }

            is TodoAction.OnTitleChanged -> {
                _state.update {
                    it.copy(
                        input = it.input.copy(
                            title = action.title,
                        )
                    )
                }
            }

            is TodoAction.OnDeleteClick -> {
                _state.update {
                    it.copy(
                        todoList = it.todoList.filter { todoItem ->
                            todoItem != action.todo
                        }
                    )
                }
            }
        }
    }
}