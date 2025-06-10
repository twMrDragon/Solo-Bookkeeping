package com.example.solobookkeeping.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TwoButtonGroup(
    modifier: Modifier = Modifier,
    value: Boolean,
    values: List<Boolean> = listOf(false, true),
    titles: List<String>,
    onClick: (Boolean) -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        values.zip(titles).forEach { (item, label) ->
            // 動畫控制背景顏色
            val backgroundColor by animateColorAsState(
                if (item == value) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "background"
            )

            // 動畫控制文字顏色
            val contentColor by animateColorAsState(
                if (item == value) Color.White else MaterialTheme.colorScheme.onSurface,
                label = "textColor"
            )

            // 動畫控制邊框寬度
            val borderWidth by animateDpAsState(
                if (item == value) 0.dp else 1.dp,
                label = "border"
            )

            // 共用樣式
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(backgroundColor)
                    .border(
                        borderWidth,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.shapes.small
                    )
                    .clickable { onClick(item) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
