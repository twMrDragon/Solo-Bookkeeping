package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.BookkeepingType
import com.example.solobookkeeping.sample.SampleData
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import java.time.LocalDate

@Composable
fun BookkeepingScreen(modifier: Modifier = Modifier) {
    val data =
        SampleData.sampleBookkeepingList.groupBy { it.date }.toSortedMap(compareByDescending { it })

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.75f)
                    .aspectRatio(1f)
                    .background(Color.Gray, shape = CircleShape) // 圓形背景
            )
        }
        items(data.entries.toList()) { (date, bookkeeping) ->
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
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                date.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            )
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
        Text(
            text = entry.title,
            modifier = Modifier
                .weight(1f)
        )
        val sign = if (entry.type == BookkeepingType.INCOME) "" else "-"
        Text(
            text = "$sign ${entry.amount}",
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun BookkeepingScreenPreview() {
    SoloBookkeepingTheme {
        BookkeepingScreen(modifier = Modifier.fillMaxSize())
    }
}