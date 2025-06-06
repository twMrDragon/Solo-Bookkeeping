package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.viewmodel.BookkeepingViewModel
import java.time.LocalDate

@Composable
fun BookkeepingScreen(
    modifier: Modifier = Modifier,
    viewModel: BookkeepingViewModel,
    onAddClick: () -> Unit
) {
    val groupedEntries by viewModel.groupedEntries.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .aspectRatio(1f)
                    .background(Color.Gray, shape = CircleShape) // 圓形背景
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
                bookkeeping = bookkeeping
            )
        }
    }
}

@Composable
fun BookkeepingCard(
    modifier: Modifier = Modifier,
    date: LocalDate,
    bookkeeping: List<Bookkeeping>
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                BookkeepingCardItem(entry = entry)
            }
        }
    }

}

@Composable
fun BookkeepingCardItem(
    modifier: Modifier = Modifier,
    entry: Bookkeeping
) {
    Row(
        modifier = modifier
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