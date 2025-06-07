package com.example.solobookkeeping.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.model.IncomeCategory
import com.example.solobookkeeping.ui.theme.SoloBookkeepingTheme
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookkeepingScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: (Bookkeeping) -> Unit,
    bookkeeping: Bookkeeping? = null
) {
    var isIncome by remember { mutableStateOf(false) }
    var selectedExpenseCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }
    var selectedIncomeCategory by remember { mutableStateOf(IncomeCategory.Salary) }
    var title by remember { mutableStateOf("") }
    var isTitleError by remember { mutableStateOf(false) }
    var depiction by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isAmountError by remember { mutableStateOf(false) }

    var selectedDateMillis by remember {
        mutableStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }

    val selectedDateText = selectedDateMillis.let {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
    }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookkeeping) {
        bookkeeping?.let {
            when (it.category) {
                is IncomeCategory -> {
                    isIncome = true
                    selectedIncomeCategory = it.category
                }

                is ExpenseCategory -> {
                    isIncome = false
                    selectedExpenseCategory = it.category
                }
            }
            title = it.title
            depiction = it.depiction
            amount = kotlin.math.abs(it.amount).toString()
            selectedDateMillis =
                it.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
            IncomeExpenseGroup(
                isIncome = isIncome,
                onClick = { isIncome = it }
            )
            if (isIncome) {
                CategoryGrid(
                    selectedCategory = selectedIncomeCategory,
                    categoryList = IncomeCategory.entries.toList(),
                    onCategorySelected = { selectedIncomeCategory = it as IncomeCategory }
                )
            } else {
                CategoryGrid(
                    selectedCategory = selectedExpenseCategory,
                    categoryList = ExpenseCategory.entries.toList(),
                    onCategorySelected = { selectedExpenseCategory = it as ExpenseCategory })
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = {
                    if (isAmountError)
                        isAmountError = false // 清除錯誤狀態
                    amount = it
                },
                label = { Text("金額") },
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
                value = title,
                onValueChange = {
                    if (isTitleError)
                        isTitleError = false // 清除錯誤狀態
                    title = it
                },
                label = { Text("標題") },
                singleLine = true,
                isError = isTitleError,
                supportingText = {
                    if (isTitleError) {
                        Text("標題不能為空", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = depiction,
                onValueChange = { depiction = it },
                label = { Text("敘述") },
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
                    Text(selectedDateText)
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
                    isTitleError = title.isBlank()
                    isAmountError = amount.isBlank() || amount.toDoubleOrNull() == null

                    if (isTitleError || isAmountError) return@Button

                    val selectedDate = selectedDateMillis.let { millis ->
                        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val bookkeeping = Bookkeeping(
                        id = bookkeeping?.id ?: 0,
                        category = (if (isIncome) selectedIncomeCategory else selectedExpenseCategory),
                        title = title,
                        depiction = depiction,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        date = selectedDate ?: LocalDate.now()
                    )
                    onConfirm(bookkeeping)
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
            ) { Text(if (bookkeeping == null) "保存" else "修改") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    initialDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun IncomeExpenseGroup(
    modifier: Modifier = Modifier,
    isIncome: Boolean,
    onClick: (Boolean) -> Unit
) {
    val value = listOf(false, true)
    val title = listOf("支出", "收入")

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        value.zip(title).forEach { (item, label) ->
            // 動畫控制背景顏色
            val backgroundColor by animateColorAsState(
                if (item == isIncome) MaterialTheme.colorScheme.primary else Color.Transparent,
                label = "background"
            )

            // 動畫控制文字顏色
            val contentColor by animateColorAsState(
                if (item == isIncome) Color.White else MaterialTheme.colorScheme.onSurface,
                label = "textColor"
            )

            // 動畫控制邊框寬度
            val borderWidth by animateDpAsState(
                if (item == isIncome) 0.dp else 1.dp,
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

@Composable
fun CategoryGrid(
    modifier: Modifier = Modifier,
    categoryList: List<Category> = ExpenseCategory.entries.toList(),
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryList.chunked(4).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowItems.map { item ->
                    val isSelected = item == selectedCategory
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(if (isSelected) item.color else Color.Transparent)
                            .padding(8.dp)
                            .clickable {
                                onCategorySelected(item)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = item.title,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
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