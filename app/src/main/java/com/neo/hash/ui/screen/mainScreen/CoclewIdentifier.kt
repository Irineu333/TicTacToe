@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui.screen.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    onAddReferenceCode: (String) -> Unit = {},
    onRemoveReferenceCode: () -> Unit = {},
) = Box(
    modifier = modifier
        .fillMaxWidth()
        .padding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
) {

    var showInsertReferenceCode by remember { mutableStateOf(false) }

    Button(
        onClick = {
            if (referenceCode.isEmpty()) {
                showInsertReferenceCode = true
            } else {
                onRemoveReferenceCode()
            }
        },
        contentPadding = PaddingValues(
            horizontal = 10.dp
        ),
        modifier = Modifier
            .align(Alignment.CenterEnd)
    ) {
        AnimatedVisibility(visible = referenceCode.isEmpty()) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Text(
            text = referenceCode.ifEmpty { "Código de referência".uppercase() },
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

    if (showInsertReferenceCode) {
        InsertReferenceCode(
            onDismissRequest = { showInsertReferenceCode = false },
            onAddReferenceCode = onAddReferenceCode
        )
    }
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
                text = "Código de referência".uppercase(),
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
            referenceCode = "KJSKDADJHAS"
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
