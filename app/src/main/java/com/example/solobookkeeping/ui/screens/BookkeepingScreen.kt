package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.ui.components.RingChart
import com.example.solobookkeeping.ui.components.RingChartSegment
import com.example.solobookkeeping.viewmodel.BookkeepingViewModel
import java.time.LocalDate

@Composable
fun BookkeepingScreen(
    modifier: Modifier = Modifier,
    viewModel: BookkeepingViewModel,
    onAddClick: () -> Unit = {},
    onEntryClick: (Bookkeeping) -> Unit = {}
) {
    val groupedEntries by viewModel.groupedEntries.collectAsState()
    val categoryRatios by viewModel.categoryRatios.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            RingChart(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .aspectRatio(1f),
                strokeWidth = 100f,
                segments = calculateCategoryRing(categoryRatios),
            )
        }

        item {
            Box(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .fillMaxWidth(0.75f)
            ) {
                Button(
                    modifier = Modifier.fillMaxSize(),
                    onClick = onAddClick
                ) {
                    Text("新增紀錄")
                }
            }
        }
        items(
            groupedEntries.entries.toList(),
            key = { it.key } // 使用日期作為 key
        ) { (date, bookkeeping) ->
            BookkeepingCard(
                date = date,
                bookkeeping = bookkeeping,
                onEntryClick = onEntryClick
            )
        }
    }
}

@Composable
fun BookkeepingCard(
    modifier: Modifier = Modifier,
    date: LocalDate,
    bookkeeping: List<Bookkeeping>,
    onEntryClick: (Bookkeeping) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row {
                Text(
                    date.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    bookkeeping.sumOf { it.amount }.let { "%.2f".format(it) },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                )
            }
            bookkeeping.forEach { entry ->
                BookkeepingCardItem(
                    entry = entry,
                    onEntryClick = onEntryClick
                )
            }
        }
    }

}

fun calculateCategoryRing(
    categoryRatios: Map<Category, Float>
): List<RingChartSegment> {
    if (categoryRatios.isEmpty()) {
        return emptyList()
    }
    val segments = mutableListOf<RingChartSegment>()
    var startAngle = 0f
    categoryRatios.forEach { (category, ratio) ->
        val endAngle = startAngle + ratio
        val color = category.color
        segments.add(RingChartSegment(start = startAngle, end = endAngle, color = color))
        startAngle = endAngle
    }
    return segments
}

@Composable
fun BookkeepingCardItem(
    modifier: Modifier = Modifier,
    entry: Bookkeeping,
    onEntryClick: (Bookkeeping) -> Unit = { /* Handle click */ }
) {
    Row(
        modifier = modifier
            .clickable {
                onEntryClick(entry)
            }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = entry.category.icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = entry.title,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = "%.2f".format(entry.amount),
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun BookkeepingScreenPreview() {
//    SoloBookkeepingTheme {
//        BookkeepingScreen(modifier = Modifier.fillMaxSize())
//    }
//}