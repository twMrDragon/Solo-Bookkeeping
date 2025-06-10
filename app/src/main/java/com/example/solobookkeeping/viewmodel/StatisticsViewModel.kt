package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Category
import com.example.solobookkeeping.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import kotlin.math.abs

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookkeepingDao()

    private val _groupedEntries = MutableStateFlow<Map<Category, List<Bookkeeping>>>(emptyMap())
    val groupedEntries = _groupedEntries

    private val _categoryRatios = MutableStateFlow<Map<Category, Float>>(emptyMap())
    val categoryRatios: StateFlow<Map<Category, Float>> = _categoryRatios

    private val _selectCategory = MutableStateFlow<Category?>(null)
    val selectCategory: StateFlow<Category?> = _selectCategory

    private val _startYear = MutableStateFlow(YearMonth.now().year)
    val startYear = _startYear
    private val _startMonth = MutableStateFlow(YearMonth.now().monthValue)
    val startMonth = _startMonth

    private val _endYear = MutableStateFlow(YearMonth.now().year)
    val endYear = _endYear
    private val _endMonth = MutableStateFlow(YearMonth.now().monthValue)
    val endMonth = _endMonth

    init {
        loadEntriesInRange(_startYear.value, _startMonth.value, _endYear.value, _endMonth.value)
    }

    fun updateEntries() {
        viewModelScope.launch {
            loadEntriesInRange(
                _startYear.value,
                _startMonth.value,
                _endYear.value,
                _endMonth.value
            )
        }
    }

    fun selectCategory(category: Category) {
        _selectCategory.value = category
    }

    fun loadEntriesInRange(startYear: Int, startMonth: Int, endYear: Int, endMonth: Int) {
        _startYear.value = startYear
        _startMonth.value = startMonth
        _endYear.value = endYear
        _endMonth.value = endMonth

        viewModelScope.launch {
            val allEntries = dao.getAll()

            val start = YearMonth.of(startYear, startMonth)
            val end = YearMonth.of(endYear, endMonth)

            val filtered = allEntries.filter {
                val entryYearMonth = YearMonth.from(it.date)
                entryYearMonth >= start && entryYearMonth <= end && it.category is ExpenseCategory
            }

            val totalExpense = filtered.sumOf { abs(it.amount) }

            val grouped = filtered.groupBy { it.category }
            _groupedEntries.value = grouped

            val ratios = if (totalExpense != 0.0) {
                grouped.mapValues { (_, entries) ->
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
}