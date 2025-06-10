package com.example.solobookkeeping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.solobookkeeping.data.AppDatabase
import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.Debt
import com.example.solobookkeeping.model.DebtType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DebtViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).debtDao()

    private val _groupedEntries =
        MutableStateFlow<Map<String, Map<DebtType, List<Debt>>>>(emptyMap())
    val groupedEntries: StateFlow<Map<String, Map<DebtType, List<Debt>>>> = _groupedEntries

    private val _currentEntry = MutableStateFlow<Debt?>(null)
    val currentEntry: StateFlow<Debt?> = _currentEntry

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense

    private val _personalDebt = MutableStateFlow<List<Debt>>(emptyList())
    val personalDebt: StateFlow<List<Debt>> = _personalDebt

    private val _who = MutableStateFlow("")
    val who: StateFlow<String> = _who

    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            dao.insert(debt)
            loadAllDebt()
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch {
            dao.update(debt)
            loadAllDebt()
            loadPersonalDebt(_who.value)
        }
    }

fun deleteDebt(debt: Debt) {
        viewModelScope.launch {
            dao.delete(debt)
            loadAllDebt()
            loadPersonalDebt(_who.value)
        }
    }

    fun setCurrentEntry(debt: Debt?) {
        _currentEntry.value = debt
    }

    fun loadPersonalDebt(who: String) {
        _who.value = who
        viewModelScope.launch {
            val debts = dao.getPersonalDebt(who)
                .sortedWith(compareBy<Debt> { it.isSettled }
                    .thenByDescending { it.borrowedDate })
            _personalDebt.value = debts
        }
    }

    private fun loadAllDebt() {
        viewModelScope.launch {
            val allEntries = dao.getAll()
            _totalIncome.value =
                allEntries.filter { !it.isSettled && it.debtType == DebtType.BORROWED }
                    .sumOf { it.amount }
            _totalExpense.value =
                allEntries.filter { !it.isSettled && it.debtType == DebtType.LENT }
                    .sumOf { it.amount }
            val grouped = allEntries.groupBy { it.who }.mapValues { (_, debts) ->
                debts.groupBy { it.debtType }
            }
            _groupedEntries.value = grouped
        }
    }

    init {
        viewModelScope.launch {
            loadAllDebt()
        }
    }
}