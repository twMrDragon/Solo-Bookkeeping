package com.example.solobookkeeping.data

import androidx.room.TypeConverter
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.model.IncomeCategory
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    @TypeConverter
    fun fromCategory(category: Category): String {
        val type = when (category) {
            is IncomeCategory -> "Income"
            is ExpenseCategory -> "Expense"
            else -> throw IllegalArgumentException("Unknown category type: ${category::class.java}")
        }
        return "$type:${(category as Enum<*>).name}"
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        val (type, name) = value.split(":")
        return when (type) {
            "Income" -> IncomeCategory.valueOf(name)
            "Expense" -> ExpenseCategory.valueOf(name)
            else -> throw IllegalArgumentException("Unknown category type: $type")
        }
    }
}