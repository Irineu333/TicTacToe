package com.neo.hashgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.neo.hashgame.ui.MainScreen
import com.neo.hashgame.ui.theme.HashGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HashGameTheme {
                // A surface container using the 'background' color from the theme
                HashGameBackground(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun HashGameBackground(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit,
) = Surface(
    modifier = modifier,
    color = color
) {
    content()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HashGameTheme {
        MainScreen()
    }
}