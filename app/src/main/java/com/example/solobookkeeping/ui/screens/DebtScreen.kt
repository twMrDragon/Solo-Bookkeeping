package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solobookkeeping.model.Debt
import com.example.solobookkeeping.model.DebtType
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import com.example.solobookkeeping.viewmodel.DebtViewModel
import java.time.LocalDate

@Composable
fun DebtScreen(
    modifier: Modifier = Modifier,
    viewModel: DebtViewModel,
    onAddClick: () -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    val groupedEntries by viewModel.groupedEntries.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            DebtDashboard(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
            )
        }
        item {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = onAddClick,
                shape = MaterialTheme.shapes.small,
            ) {
                Text("新增紀錄")
            }
        }
        items(groupedEntries.entries.toList()) { (who, debt) ->
            DebtCard(
                who = who, debt = debt,
                onCardClick = onCardClick,
            )
        }
    }
}

@Composable
fun DebtCard(
    modifier: Modifier = Modifier,
    who: String,
    debt: Map<DebtType, List<Debt>>,
    onCardClick: (String) -> Unit = {}
) {
    val borrowed = debt[DebtType.BORROWED]?.filter { !it.isSettled }?.sumOf { it.amount } ?: 0.0
    val lent = debt[DebtType.LENT]?.filter { !it.isSettled }?.sumOf { it.amount } ?: 0.0

    val (borrowedRatio, lentRatio) = calculateRatios(borrowed, lent)

    ElevatedCard(
        modifier = modifier.fillMaxSize(),
        onClick = { onCardClick(who) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row {
                Text(
                    who,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                DetermineSentiment(
                    borrowed = borrowed,
                    lent = lent
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                progress = borrowedRatio,
                color = com.example.solobookkeeping.ui.theme.gain,
                amount = borrowed
            )
            Spacer(modifier = Modifier.height(4.dp))
            ProgressBar(
                progress = lentRatio,
                color = com.example.solobookkeeping.ui.theme.loss,
                amount = lent,
                flip = true,
            )
        }
    }
}

@Composable
fun DetermineSentiment(
    modifier: Modifier = Modifier,
    borrowed: Double = 0.0, lent: Double = 0.0
) {
    val total = borrowed + lent
    val borrowedRatio = if (total == 0.0) 0.5 else borrowed / total

    val icon = when {
        borrowedRatio >= 0.8 -> Icons.Default.SentimentVerySatisfied
        borrowedRatio >= 0.6 -> Icons.Default.SentimentSatisfied
        borrowedRatio >= 0.4 -> Icons.Default.SentimentNeutral
        borrowedRatio >= 0.2 -> Icons.Default.SentimentDissatisfied
        else -> Icons.Default.SentimentVeryDissatisfied
    }

    Icon(
        imageVector = icon,
        contentDescription = "Sentiment Icon"
    )
}

fun calculateRatios(
    borrowed: Double, lent: Double
): Pair<Float, Float> {
    return if (borrowed == 0.0 && lent == 0.0) {
        Pair(0.0f, 0.0f) // Default values if no data
    } else if (borrowed > lent) {
        Pair(1.0f, (lent / borrowed).toFloat())
    } else if (lent > borrowed) {
        Pair((borrowed / lent).toFloat(), 1.0f)
    } else {
        Pair(1.0f, 1.0f)
    }
}

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier, amount: Double = 0.0, progress: Float = 0f, color: Color,
    flip: Boolean = false,
    progressHeight: Dp = 8.dp
) {
    if (!flip) {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                "$%.2f".format(amount),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
            Box(
                modifier = Modifier
                    .height(progressHeight)
                    .defaultMinSize(minWidth = progressHeight * 2)
                    .fillMaxWidth(progress)
                    .clip(MaterialTheme.shapes.small)
                    .background(color),
            )
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .height(progressHeight)
                    .defaultMinSize(minWidth = progressHeight * 2)
                    .fillMaxWidth(progress)
                    .clip(MaterialTheme.shapes.small)
                    .background(color),
            )
            Text(
                "$%.2f".format(amount),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
        }

    }
}

@Composable
fun DebtDashboard(
    modifier: Modifier = Modifier, totalIncome: Double = 0.0, totalExpense: Double = 0.0
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.weight(1f), colors = CardDefaults.elevatedCardColors(
                containerColor = com.example.solobookkeeping.ui.theme.gain,
                contentColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null)
                Text(
                    "$%.2f".format(totalIncome),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        ElevatedCard(
            modifier = Modifier.weight(1f), colors = CardDefaults.elevatedCardColors(
                containerColor = com.example.solobookkeeping.ui.theme.loss,
                contentColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.MoneyOff, contentDescription = null)
                Text(
                    "$%.2f".format(totalExpense),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DebtScreenPreview() {
    SoloBookkeepingTheme {
        DebtScreen(
            modifier = Modifier.padding(16.dp),
            viewModel = viewModel()
        )
    }
}