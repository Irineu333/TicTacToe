@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui.screen.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hash.R
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.GameDialog
import com.neo.hash.ui.theme.HashTheme

@Composable
fun CoclewIdentifier(
    modifier: Modifier = Modifier,
    referenceCode: String = "",
    isMaintenance: Boolean = true,
    showMaintenance: () -> Unit = {},
    onAddReferenceCode: (String) -> Unit = {},
    onRemoveReferenceCode: () -> Unit = {},
) = Box(
    modifier = modifier.fillMaxWidth()
) {

    var showInsertReferenceCode by remember { mutableStateOf(false) }
    var showRemoveReferenceCode by remember { mutableStateOf(false) }

    val primaryColor by animateColorAsState(if (isMaintenance) Color.Red else colors.primary)

    MaterialTheme(colors.copy(primaryColor)) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AnimatedVisibility(isMaintenance) {

                Card(
                    modifier = Modifier
                        .defaultMinSize(
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(
                            end = 8.dp
                        )
                        .clickable(onClick = showMaintenance),
                    backgroundColor = Color.Red
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (referenceCode.isEmpty()) {
                        showInsertReferenceCode = true
                    } else {
                        showRemoveReferenceCode = true
                    }
                },
                contentPadding = PaddingValues(
                    horizontal = 10.dp
                )
            ) {
                AnimatedVisibility(visible = referenceCode.isEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Text(
                    text = referenceCode.ifEmpty { stringResource(R.string.title_refeence_code) },
                    letterSpacing = 0.sp,
                    lineHeight = 0.sp
                )

                AnimatedVisibility(visible = referenceCode.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.TwoTone.Cancel,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }

    if (showInsertReferenceCode) {
        InsertReferenceCode(
            onDismissRequest = { showInsertReferenceCode = false },
            onAddReferenceCode = onAddReferenceCode
        )
    }

    if (showRemoveReferenceCode) {
        RemoveReferenceCode(
            onDismissRequest = {
                showRemoveReferenceCode = false
            },
            onRemoveReferenceCode = onRemoveReferenceCode
        )
    }
}

@Composable
private fun RemoveReferenceCode(
    onDismissRequest: () -> Unit = {},
    onRemoveReferenceCode: () -> Unit = {}
) {
    GameDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        title = {
            Text(
                text = stringResource(R.string.title_remove_code),
                modifier = Modifier.align(Alignment.Center)
            )
        },
        content = {
            Text(
                text = stringResource(R.string.text_remove_code),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        buttons = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.btn_not))
            }

            TextButton(
                onClick = {
                    onDismissRequest()
                    onRemoveReferenceCode()
                }
            ) {
                Text(text = stringResource(R.string.btn_yes))
            }
        },
        buttonsArrangement = Arrangement.SpaceAround
    )
}

@Composable
fun InsertReferenceCode(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onAddReferenceCode: (String) -> Unit = {}
) {
    var referenceCode by remember { mutableStateOf("") }

    GameDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.title_refeence_code),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        },
        buttons = {
            GameButton(
                onClick = {
                    onAddReferenceCode(referenceCode)
                    onDismissRequest()
                },
                enabled = referenceCode.isNotBlank()
            ) {
                Text(text = stringResource(id = R.string.btn_confirm).uppercase())
            }
        },
        content = {

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = referenceCode,
                onValueChange = {
                    referenceCode = it.trim()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Preview("Reference Code")
@Composable
private fun CoclewIdentifierAddPreview() {
    HashTheme {
        CoclewIdentifier(
            referenceCode = "IRINEU_TEST"
        )
    }
}

@Preview("Reference Code Disable")
@Composable
private fun CoclewIdentifierDisablePreview() {
    HashTheme {
        CoclewIdentifier(
            referenceCode = "IRINEU_TEST",
            isMaintenance = false
        )
    }
}

@Preview(name = "Add Reference Code Button")
@Composable
private fun CoclewIdentifierRemovePreview() {
    HashTheme {
        CoclewIdentifier()
    }
}

@Preview(name = "Insert Reference Code Dialog")
@Composable
private fun InsertReferenceCodePreview() {
    HashTheme {
        InsertReferenceCode()
    }
}

@Preview(name = "Remove Reference Code Dialog")
@Composable
private fun RemoveReferenceCodePreview() {
    HashTheme {
        RemoveReferenceCode()
    }
}
