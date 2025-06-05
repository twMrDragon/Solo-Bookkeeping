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

class BookkeepingViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).bookkeepingDao()

    private val _groupedEntries = MutableStateFlow<Map<LocalDate, List<Bookkeeping>>>(emptyMap())
    val groupedEntries: StateFlow<Map<LocalDate, List<Bookkeeping>>> = _groupedEntries

    init {
        viewModelScope.launch {
            val allEntries = dao.getAll()
            val groupedSorted = allEntries.groupBy { it.date }
                .toSortedMap(compareByDescending { it })
            _groupedEntries.value = groupedSorted
        }
    }

    fun addBookkeeping(entry: Bookkeeping) {
        viewModelScope.launch {
            dao.insert(entry)
            val allEntries = dao.getAll()
            val groupedSorted = allEntries.groupBy { it.date }
                .toSortedMap(compareByDescending { it })
            _groupedEntries.value = groupedSorted
        }
    }
}