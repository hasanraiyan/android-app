package com.ai.pyqdeck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.models.Semester
import com.ai.pyqdeck.utils.DataParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SemesterViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    private val _semesters = MutableStateFlow<List<Semester>>(emptyList())
    val semesters: StateFlow<List<Semester>> = _semesters

    private val branchId: String = savedStateHandle.get<String>("branchId") ?: ""

    init {
        loadSemesters()
    }

    private fun loadSemesters() {
        viewModelScope.launch {
            val universityData = DataParser.loadUniversityData(getApplication<Application>().applicationContext)
            val branch = universityData?.branches?.find { it.id == branchId }
            _semesters.value = branch?.semesters ?: emptyList()
        }
    }
}