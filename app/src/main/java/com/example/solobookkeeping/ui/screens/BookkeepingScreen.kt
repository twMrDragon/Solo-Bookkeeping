package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme

@Composable
fun BookkeepingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text("Bookkeeping Screen")
    }
}

@Preview
@Composable
private fun BookkeepingScreenPreview() {
    SoloBookkeepingTheme {
        BookkeepingScreen()
    }
}