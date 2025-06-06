package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.abs

class BookkeepingViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookkeepingDao()
    private var currentYear: Int = YearMonth.now().year
    private var currentMonth: Int = YearMonth.now().monthValue

    private val _groupedEntries = MutableStateFlow<Map<LocalDate, List<Bookkeeping>>>(emptyMap())
    val groupedEntries: StateFlow<Map<LocalDate, List<Bookkeeping>>> = _groupedEntries

    private val _currentEntry = MutableStateFlow<Bookkeeping?>(null)
    val currentEntry: StateFlow<Bookkeeping?> = _currentEntry

    private val _categoryRatios = MutableStateFlow<Map<Category, Float>>(emptyMap())
    val categoryRatios: StateFlow<Map<Category, Float>> = _categoryRatios

    init {
        loadEntriesByYearMonth(currentYear, currentMonth)
    }

    fun addBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.insert(entry)
            loadEntriesByYearMonth(currentYear, currentMonth)
        }
    }

    fun updateBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.update(entry)
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
            }
            val grouped = filtered.groupBy { it.date }.toSortedMap(compareByDescending { it })
            _groupedEntries.value = grouped

            val expenses = filtered.filter { it.amount < 0 }

            val totalExpense = expenses.sumOf { abs(it.amount) }
            val ratios = if (totalExpense != 0.0) {
                expenses
                    .groupBy { it.category }
                    .mapValues { (_, entries) ->
                        val categorySum = entries.sumOf { abs(it.amount) }
                        (categorySum / totalExpense).toFloat()
                    }
                    .toList()
                    .sortedByDescending { (_, ratio) -> ratio }
                    .toMap()
            } else {
                emptyMap()
            }

            _categoryRatios.value = ratios
        }
    }

    fun setCurrentEntry(entry: Bookkeeping?) {
        _currentEntry.value = entry
    }
}