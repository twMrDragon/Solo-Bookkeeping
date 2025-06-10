package com.example.solobookkeeping.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Debt
import com.example.solobookkeeping.model.DebtType
import com.example.solobookkeeping.ui.components.TwoButtonGroup
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale


@Composable
fun EditDebtScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onConfirm: (Debt) -> Unit = {},
    debt: Debt? = null,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var debtType by remember { mutableStateOf(DebtType.BORROWED) }
    var isSettled by remember { mutableStateOf(false) }

    var selectedDateMillis by remember {
        mutableStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    val selectedDateText = selectedDateMillis.let {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
    }

    var isAmountError by remember { mutableStateOf(false) }
    var isNameError by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(debt) {
        debt?.let {
            name = it.who
            description = it.description
            amount = it.amount.toString()
            debtType = it.debtType
            isSettled = it.isSettled
            selectedDateMillis =
                it.borrowedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TwoButtonGroup(
                value = debtType == DebtType.LENT,
                titles = listOf("Borrowed", "Lent"),
                onClick = {
                    debtType = if (it) {
                        DebtType.LENT
                    } else {
                        DebtType.BORROWED
                    }
                },
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = {
                    if (isAmountError)
                        isAmountError = false // 清除錯誤狀態
                    amount = it
                },
                label = { Text("金額") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = isAmountError,
                supportingText = {
                    if (isAmountError) {
                        Text("請輸入有效的金額（> 0）", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    if (isNameError)
                        isNameError = false // 清除錯誤狀態
                    name = it
                },
                label = { Text("姓名") },
                singleLine = true,
                isError = isNameError,
                supportingText = {
                    if (isNameError) {
                        Text("姓名不能為空", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                singleLine = true,
                onValueChange = { description = it },
                label = { Text("敘述") },
            )

            TwoButtonGroup(
                value = isSettled,
                titles = listOf("Not yet", "Already"),
                onClick = { isSettled = it },
            )
            Button(
                onClick = {
                    showDialog = true
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Borrowed date: ${selectedDateText}")
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                }
            }
            if (showDialog) {
                DatePickerModal(
                    initialDateMillis = selectedDateMillis,
                    onDateSelected = { millis ->
                        selectedDateMillis = millis ?: selectedDateMillis
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onCancel, modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
            ) { Text("取消") }
            Button(
                onClick = {
                    isNameError = name.isBlank()
                    isAmountError = amount.isBlank() || amount.toDoubleOrNull() == null

                    if (isNameError || isAmountError) return@Button

                    val selectedDate = selectedDateMillis.let { millis ->
                        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    val debt = Debt(
                        id = debt?.id ?: 0,
                        who = name,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        debtType = debtType,
                        borrowedDate = selectedDate,
                        dueDate = null, // 可以根據需要添加到 UI 中
                        description = description,
                        isSettled = isSettled
                    )
                    onConfirm(debt)
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
            ) { Text(if (debt == null) "保存" else "修改") }
        }
    }
}
