package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.MoneyOff
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solobookkeeping.model.Debt
import com.example.solobookkeeping.model.DebtType
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import com.example.solobookkeeping.viewmodel.DebtViewModel
import java.time.LocalDate

@Composable
fun DebtScreen(modifier: Modifier = Modifier) {
    val viewModel: DebtViewModel = viewModel()
    val debts by viewModel.debts.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            DebtDashboard()
        }
        item {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    val debt = Debt(
                        who = "John Doe",
                        amount = 1000.0,
                        debtType = DebtType.BORROWED,
                        borrowedDate = LocalDate.now(),
                    )

                    viewModel.addDebt(debt)
                },
                shape = MaterialTheme.shapes.small,
            ) {
                Text("新增紀錄")
            }
        }
        items(debts) { debt ->
            DebtCard(
                debt = debt
            )
        }
    }
}

@Composable
fun DebtCard(
    modifier: Modifier = Modifier,
    debt: Debt
) {
    ElevatedCard(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                debt.who, modifier = modifier.weight(1f)
            )
            Text(
                "3 筆", modifier = modifier.weight(1f)
            )
            Text(
                "%.2f".format(debt.amount), modifier = modifier.weight(1f),
            )
        }
    }
}

@Composable
fun DebtDashboard(modifier: Modifier = Modifier) {
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
                    "$1000",
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
                    "$1000",
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
            modifier = Modifier.padding(16.dp)
        )
    }
}