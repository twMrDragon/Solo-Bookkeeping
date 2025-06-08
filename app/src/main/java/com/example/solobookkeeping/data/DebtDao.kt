package com.example.solobookkeeping.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.solobookkeeping.model.Debt

@Dao
interface DebtDao {
    @Insert
    suspend fun insert(debt: Debt)

    @Query("SELECT * FROM debt")
    suspend fun getAll(): List<Debt>

    @Update
    suspend fun update(debt: Debt)

    @Delete
    suspend fun delete(debt: Debt)
}
