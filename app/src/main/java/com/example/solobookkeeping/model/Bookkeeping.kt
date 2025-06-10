package com.example.solobookkeeping.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "bookkeeping")
data class Bookkeeping(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: Category,
    val title: String,
    val description: String,
    val amount: Double,
    val date: LocalDate
)