package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Debt
import com.example.solobookkeeping.model.DebtType
import com.example.solobookkeeping.viewmodel.DebtViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListPersonalDebtScreen(
    modifier: Modifier = Modifier,
    debtViewModel: DebtViewModel,
    onCardLongClick: (Debt) -> Unit = {},
    onCardClick: (Debt) -> Unit = {}
) {
    val personalDebt by debtViewModel.personalDebt.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(personalDebt) { debt ->
            var backgroundColor = if (debt.debtType == DebtType.BORROWED)
                com.example.solobookkeeping.ui.theme.gain else com.example.solobookkeeping.ui.theme.loss
            backgroundColor =
                if (debt.isSettled) backgroundColor.copy(alpha = 0.3f) else backgroundColor
            val interactionSource = remember { MutableInteractionSource() }
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(),
                        onClick = {
                            onCardClick(debt)
                        },
                        onLongClick = {
                            onCardLongClick(debt)
                        }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Row(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Amount: ${debt.amount}",
                        )
                        Text(
                            text = "Borrowed Date: ${debt.borrowedDate}",
                        )
                        Text(
                            text = "Description: ${debt.description}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis // 尾端顯示 ...
                        )
                    }
                    IconButton(
                        onClick = {
                            debtViewModel.updateDebt(debt.copy(isSettled = !debt.isSettled))
                        },
                        modifier = modifier.padding(8.dp)
                    ) {
                        if (debt.isSettled) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Settled")
                        } else {
                            Icon(Icons.Default.Cancel, contentDescription = "Settled")
                        }
                    }
                }
            }
        }
    }
}

