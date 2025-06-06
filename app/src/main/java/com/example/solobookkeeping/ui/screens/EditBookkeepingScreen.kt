package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookkeepingScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: (Bookkeeping) -> Unit,
    bookkeeping: Bookkeeping? = null
) {
    var isIncome by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(Category.FOOD) }
    var title by remember { mutableStateOf("") }
    var depiction by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val todayMillis =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis
    )

    LaunchedEffect(bookkeeping) {
        bookkeeping?.let {
            isIncome = it.amount >= 0
            title = it.title
            depiction = it.depiction
            amount = kotlin.math.abs(it.amount).toString()
            selectedCategory = it.category // 如果有這個欄位
            datePickerState.selectedDateMillis =
                it.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    Box {
        Column(
            modifier = modifier
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "支出")
                Switch(checked = isIncome, onCheckedChange = { isIncome = it })
                Text(text = "收入")
            }
            CategoryGrid(
                selectedCategory = selectedCategory, onCategorySelected = { selectedCategory = it })
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = { amount = it },
                label = { Text("金額") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = { title = it },
                label = { Text("標題") })
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = depiction,
                onValueChange = { depiction = it },
                label = { Text("敘述") })
            DatePicker(datePickerState)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onCancel, modifier = Modifier.weight(1f)
            ) { Text("取消") }
            Button(
                onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val bookkeeping = Bookkeeping(
                        id = bookkeeping?.id ?: 0,
                        category = selectedCategory,
                        title = title,
                        depiction = depiction,
                        amount = (amount.toDoubleOrNull() ?: 0.0) * if (isIncome) 1 else -1,
                        date = selectedDate ?: LocalDate.now()
                    )
                    onConfirm(bookkeeping)
                }, modifier = Modifier.weight(1f)
            ) { Text(if (bookkeeping == null) "保存" else "修改") }
        }
    }
}

@Composable
fun CategoryGrid(
    modifier: Modifier = Modifier,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Category.entries.toTypedArray().toList().chunked(4).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in rowItems) {
                    val isSelected = item == selectedCategory
                    Column(modifier = Modifier
                        .clickable {
                            onCategorySelected(item)
                        }
                        .weight(1f)
                        .aspectRatio(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Icon(
                            item.icon,
                            contentDescription = item.name,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = item.title,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SoloBookkeepingTheme {
        EditBookkeepingScreen(modifier = Modifier, onCancel = {}, onConfirm = {})
    }
}