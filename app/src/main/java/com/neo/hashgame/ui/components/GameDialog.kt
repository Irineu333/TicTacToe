package com.neo.hashgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun GameDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) = Dialog(
    onDismissRequest = onDismissRequest
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
    ) {
        Column(
            modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            title?.let {
                val textStyle = MaterialTheme.typography.subtitle1
                ProvideTextStyle(textStyle, title)
            }
            content()
            buttons?.invoke()
        }
    }
}

@Preview
@Composable
fun GameDialogPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        GameDialog(
            title = {
                Text(text = "Title")
            }
        ) {
            var text by remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text(text = "Test")
                }
            )
        }
    }
}