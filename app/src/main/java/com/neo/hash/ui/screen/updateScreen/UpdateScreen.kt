package com.neo.hash.ui.screen.updateScreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neo.hash.BuildConfig
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.GameDialog
import com.neo.hash.ui.screen.updateScreen.viewModel.UpdateViewModel
import com.neo.hash.ui.theme.HashTheme

@Composable
fun UpdateScreen(
    viewModel: UpdateViewModel = viewModel(),
    openPlayStore: () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState().value
    val update = state.update

    val currentVersion = BuildConfig.VERSION_CODE

    var showDialog by remember { mutableStateOf(true) }

    if (
        update != null &&
        currentVersion < update.lastVersion &&
        showDialog
    ) {
        val cancelable = currentVersion >= update.lastRequiredVersion

        UpdateDialog(
            versionName = update.lastVersionName,
            onDismissRequest = {
                if (cancelable) {
                    showDialog = false
                }
            },
            cancelable = cancelable,
            openPlayStore = openPlayStore
        )
    }
}

@Composable
private fun UpdateDialog(
    versionName: String,
    cancelable: Boolean = true,
    onDismissRequest: () -> Unit = {},
    openPlayStore: () -> Unit = {},
) {
    GameDialog(
        title = {
            Text(
                text = "Atualização".uppercase(),
                modifier = Modifier
                    .align(Alignment.Center),
                color = MaterialTheme.colors.primary
            )
        },
        buttons = {

            if (cancelable) {
                GameButton(
                    onClick = onDismissRequest
                ) {
                    Text(text = "Depois".uppercase())
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            GameButton(
                onClick = openPlayStore
            ) {
                Text(text = "Atualizar".uppercase())
            }
        },
        onDismissRequest = onDismissRequest
    ) {
        Text(
            text = buildAnnotatedString {

                append("A versão ".uppercase())

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(versionName)
                }

                append(" está disponível na PlayStore".uppercase())
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun UpdateDialogPreview() {
    HashTheme {
        UpdateDialog(
            versionName = "1.0.0"
        )
    }
}
