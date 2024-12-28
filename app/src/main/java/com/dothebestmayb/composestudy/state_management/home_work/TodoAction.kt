package com.dothebestmayb.composestudy.state_management.home_work

sealed interface TodoAction {
    data class OnTitleChanged(val title: String): TodoAction
    data class OnDescriptionChanged(val description: String): TodoAction
    data class OnCheckClick(val todo: Todo): TodoAction
    data class OnDeleteClick(val todo: Todo): TodoAction
    data object OnAddClick: TodoAction
}