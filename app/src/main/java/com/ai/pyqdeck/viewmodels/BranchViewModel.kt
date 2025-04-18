package com.ai.pyqdeck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.models.Branch
import com.ai.pyqdeck.utils.DataParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BranchViewModel(application: Application) : AndroidViewModel(application) {

    private val _branches = MutableStateFlow<List<Branch>>(emptyList())
    val branches: StateFlow<List<Branch>> = _branches

    init {
        loadBranches()
    }

    private fun loadBranches() {
        viewModelScope.launch {
            val universityData = DataParser.loadUniversityData(getApplication<Application>().applicationContext)
            _branches.value = universityData?.branches ?: emptyList()
        }
    }
}