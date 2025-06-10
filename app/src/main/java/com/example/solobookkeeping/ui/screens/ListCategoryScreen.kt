package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.viewmodel.StatisticsViewModel
import java.time.format.DateTimeFormatter


@Composable
fun ListCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel,
    category: Category
) {
    val groupEntries = viewModel.groupedEntries.collectAsState()
    val categoryEntries = groupEntries.value[category] ?: emptyList()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categoryEntries) { entry ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min) // 確保左側色條高度等於內容高度
                ) {
                    // 左側色條
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .fillMaxHeight()
                            .background(entry.category.color)
                    )

                    // 內容
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = entry.date.format(formatter),
                        )
                        Text(
                            text = entry.title,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = "%.2f".format(entry.amount),
                        )
                    }
                }
            }
        }
    }
}
