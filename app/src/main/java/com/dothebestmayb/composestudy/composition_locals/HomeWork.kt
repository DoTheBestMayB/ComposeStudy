package com.dothebestmayb.composestudy.composition_locals

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.launch

private val LocalSnackbarState = compositionLocalOf {
    SnackbarHostState()
}

@Composable
fun SnackBarComposition(modifier: Modifier = Modifier) {
    val state = LocalSnackbarState.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(state)
        }
    ) { innerPadding ->
        Button(
            modifier = Modifier.padding(innerPadding),
            onClick = {
                scope.launch {
                    state.showSnackbar("Hello World")
                }
            }
        ) {
            Text("Click me")
        }
    }

}

@Preview
@Composable
private fun SnackBarCompositionPreview() {
    ComposeStudyTheme {
        SnackBarComposition()
    }
}
