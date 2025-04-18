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

class SubjectDetailsViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {

    private val _subjectDetails = MutableStateFlow<Subject?>(null)
    val subjectDetails: StateFlow<Subject?> = _subjectDetails

    // Assuming subjectId is passed via SavedStateHandle from navigation
    private val subjectId: String = savedStateHandle.get<String>("subjectId") ?: ""

    init {
        loadSubjectDetails()
    }

    private fun loadSubjectDetails() {
        viewModelScope.launch {
            val universityData = DataParser.loadUniversityData(getApplication<Application>().applicationContext)
            // Find the correct subject across all branches and semesters
            val subject = universityData?.branches
                ?.flatMap { it.semesters }
                ?.flatMap { it.subjects }
                ?.find { it.id == subjectId }
            _subjectDetails.value = subject
        }
    }
}