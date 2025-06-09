package com.example.solobookkeeping.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    title: String,
    text: String,
    onConfirm: () -> Unit = { },
    onDismiss: () -> Unit = {}
) {
    if (show) {
        AlertDialog(onDismissRequest = onDismiss, title = {
            Text(title)
        }, text = {
            Text(text)
        }, confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("刪除", color = MaterialTheme.colorScheme.error)
            }
        }, dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        })
    }

}