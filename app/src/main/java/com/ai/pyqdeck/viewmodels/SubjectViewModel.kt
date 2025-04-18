package com.ai.pyqdeck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.models.Subject
import com.ai.pyqdeck.utils.DataParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    // Assuming semesterId is passed via SavedStateHandle from navigation
    private val semesterId: String = savedStateHandle.get<String>("semesterId") ?: ""

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            val universityData = DataParser.loadUniversityData(getApplication<Application>().applicationContext)
            // Find the correct semester across all branches
            val semester = universityData?.branches?.flatMap { it.semesters }?.find { it.id == semesterId }
            _subjects.value = semester?.subjects ?: emptyList()
        }
    }
}