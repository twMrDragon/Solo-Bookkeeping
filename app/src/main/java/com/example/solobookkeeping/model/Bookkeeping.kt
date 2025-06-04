package com.example.solobookkeeping.model

import java.time.LocalDate

data class Bookkeeping(
    val title: String,
    val depiction: String,
    val type: BookkeepingType, // Default type is Expense
    val amount: Double,
    val date: LocalDate
)

enum class BookkeepingType {
    INCOME, EXPENSE
}
