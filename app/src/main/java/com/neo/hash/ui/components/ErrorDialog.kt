package com.neo.hash.ui.components

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit = {}
) = GameDialog(
    onDismissRequest = { },
    title = {
        Text(
            text = "OCORREU UM ERRO",
            modifier = Modifier.align(Alignment.Center)
        )
    },
    content = {
        Text(text = message)
    },
    buttons = {
        TextButton(
            onClick = onDismiss
        ) {
            Text(text = "OK")
        }
    }
)

@Preview
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog("message")
}