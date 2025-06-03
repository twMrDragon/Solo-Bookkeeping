package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme

@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text("Statistics Screen")
    }
}

@Preview
@Composable
private fun StatisticsScreenPreview() {
    SoloBookkeepingTheme {
        StatisticsScreen()
    }
}