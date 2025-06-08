package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Debt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DebtViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).debtDao()

    private val _debts = MutableStateFlow<List<Debt>>(emptyList())
    val debts: StateFlow<List<Debt>> = _debts

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            dao.insert(debt)
            _debts.value = dao.getAll()
        }
    }

    init {
        viewModelScope.launch {
            _debts.value = dao.getAll()
        }
    }
}