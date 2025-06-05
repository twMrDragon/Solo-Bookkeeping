package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Bookkeeping
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class BookkeepingViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookkeepingDao()
    private var currentYear: Int = YearMonth.now().year
    private var currentMonth: Int = YearMonth.now().monthValue


    private val _groupedEntries = MutableStateFlow<Map<LocalDate, List<Bookkeeping>>>(emptyMap())
    val groupedEntries: StateFlow<Map<LocalDate, List<Bookkeeping>>> = _groupedEntries

    init {
        loadEntriesByYearMonth(currentYear, currentMonth)
    }

    fun addBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.insert(entry)
            loadEntriesByYearMonth(currentYear, currentMonth)
        }
    }

    fun loadEntriesByYearMonth(year: Int, month: Int) {
        currentYear = year
        currentMonth = month

        viewModelScope.launch {
            val allEntries = dao.getAll()
            val filtered = allEntries.filter {
                it.date.year == year && it.date.monthValue == month
            }.groupBy { it.date }
                .toSortedMap(compareByDescending { it })

            _groupedEntries.value = filtered
        }
    }

}