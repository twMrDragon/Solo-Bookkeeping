package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import com.example.solobookkeeping.model.IncomeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.abs

class BookkeepingViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookkeepingDao()
    private val _currentYear = MutableStateFlow(YearMonth.now().year)
    val currentYear = _currentYear
    private val _currentMonth = MutableStateFlow(YearMonth.now().monthValue)
    val currentMonth = _currentMonth

    private val _groupedEntries = MutableStateFlow<Map<LocalDate, List<Bookkeeping>>>(emptyMap())
    val groupedEntries: StateFlow<Map<LocalDate, List<Bookkeeping>>> = _groupedEntries

    private val _currentEntry = MutableStateFlow<Bookkeeping?>(null)
    val currentEntry: StateFlow<Bookkeeping?> = _currentEntry

    private val _categoryRatios = MutableStateFlow<Map<Category, Float>>(emptyMap())
    val categoryRatios: StateFlow<Map<Category, Float>> = _categoryRatios

    private val _monthIncome = MutableStateFlow(0.0)
    val monthIncome: StateFlow<Double> = _monthIncome

    private val _monthExpense = MutableStateFlow(0.0)
    val monthExpense: StateFlow<Double> = _monthExpense

    init {
        loadEntriesByYearMonth(_currentYear.value, _currentMonth.value)
    }

    fun addBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.insert(entry)
            loadEntriesByYearMonth(_currentYear.value, _currentMonth.value)
        }
    }

    fun updateBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.update(entry)
            loadEntriesByYearMonth(_currentYear.value, _currentMonth.value)
        }
    }

    fun deleteBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.delete(entry)
            loadEntriesByYearMonth(_currentYear.value, _currentMonth.value)
        }
    }

    fun loadEntriesByYearMonth(year: Int, month: Int) {
        _currentYear.value = year
        _currentMonth.value = month

        viewModelScope.launch {
            val allEntries = dao.getAll()
            val filtered = allEntries.filter {
                it.date.year == year && it.date.monthValue == month
            }
            val grouped = filtered.groupBy { it.date }.toSortedMap(compareByDescending { it })
            _groupedEntries.value = grouped

            _monthIncome.value = filtered.filter { it.category is IncomeCategory }
                .sumOf { it.amount }
            _monthExpense.value = filtered.filter { it.category is ExpenseCategory }
                .sumOf { it.amount }

            val expenses = filtered.filter { it.category is ExpenseCategory }

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