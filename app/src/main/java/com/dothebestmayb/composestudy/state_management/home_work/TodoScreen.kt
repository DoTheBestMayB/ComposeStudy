package com.dothebestmayb.composestudy.state_management.home_work

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

@Composable
fun TodoScreenRoot(modifier: Modifier = Modifier) {
    val viewModel = viewModel<TodoScreenViewModel>()
    val todoState by viewModel.state.collectAsStateWithLifecycle()

    TodoScreen(
        todoState = todoState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
fun TodoScreen(
    todoState: TodoState,
    onAction: (TodoAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = todoState.input.title,
                onValueChange = { newTitle ->
                    onAction(TodoAction.OnTitleChanged(newTitle))
                },
            )
        }

        item {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = todoState.input.description,
                onValueChange = { newDescription ->
                    onAction(TodoAction.OnDescriptionChanged(newDescription))
                },
            )
        }

        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = todoState.input.title.isNotEmpty(),
                onClick = {
                    onAction(TodoAction.OnAddClick)
                }
            ) {
                Text(
                    text = "추가하기"
                )
            }
        }

        items(todoState.todoList, key = { it.key }) { todo ->
            TodoItem(
                todo = todo,
                onCheckedChange = { onAction(TodoAction.OnCheckClick(todo)) },
                onDeleteClick = { onAction(TodoAction.OnDeleteClick(todo)) }
            )
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = todo.title,
                fontWeight = FontWeight.Bold,
                textDecoration = if (todo.isChecked) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )
            Text(
                text = todo.description,
                textDecoration = if (todo.isChecked) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )
        }
        Column {
            Checkbox(
                checked = todo.isChecked,
                onCheckedChange = onCheckedChange,
            )
        }
        Column {
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun TodoScreenPreview() {
    ComposeStudyTheme {
        TodoScreen(
            onAction = {},
            todoState = TodoState(
                todoList = listOf(
                    Todo(
                        title = "hello",
                        description = "world!"
                    ),
                    Todo(
                        title = "Android Android Android Android Android Android Android Android Android Android ",
                        description = "Studio! Studio!Studio!Studio!Studio!Studio!Studio!Studio!Studio!Studio!Studio!Studio!"
                    )
                )
            ),
        )
    }
}