package com.example.solobookkeeping.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "debt")
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val who: String,
    val amount: Double,
    val description: String,
    val debtType: DebtType,
    val borrowedDate: LocalDate,
    val dueDate: LocalDate? = null,
    val isSettled: Boolean = false,
)

enum class DebtType {
    BORROWED, // 我跟別人借錢
    LENT      // 別人跟我借錢
}