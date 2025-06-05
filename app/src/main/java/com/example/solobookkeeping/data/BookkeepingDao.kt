package com.example.solobookkeeping.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.solobookkeeping.model.Bookkeeping

@Dao
interface BookkeepingDao {
    @Insert
    suspend fun insert(bookkeeping: Bookkeeping)

    @Query("SELECT * FROM bookkeeping")
    suspend fun getAll(): List<Bookkeeping>
}