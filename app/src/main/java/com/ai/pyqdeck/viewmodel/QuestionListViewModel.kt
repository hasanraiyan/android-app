package com.ai.pyqdeck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.data.DataRepository
import com.ai.pyqdeck.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionListViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = DataRepository(application)
    val branchId: String = savedStateHandle.get<String>("branchId") ?: ""
    val semesterId: String = savedStateHandle.get<String>("semesterId") ?: ""
    val subjectId: String = savedStateHandle.get<String>("subjectId") ?: ""

    // Group questions by year
    private val _questionsByYear = MutableStateFlow<Map<Int, List<Question>>>(emptyMap())
    val questionsByYear: StateFlow<Map<Int, List<Question>>> = _questionsByYear.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

     private val _subjectName = MutableStateFlow<String>("")
     val subjectName: StateFlow<String> = _subjectName.asStateFlow()


    init {
        if (branchId.isNotEmpty() && semesterId.isNotEmpty() && subjectId.isNotEmpty()) {
            loadQuestions()
            loadSubjectName()
        } else {
            _error.value = "Required IDs not provided"
        }
    }

     private fun loadSubjectName() {
         viewModelScope.launch {
             repository.getSubject(branchId, semesterId, subjectId)
                 .onSuccess { subject -> _subjectName.value = subject?.name ?: "Unknown Subject" }
                 .onFailure { _subjectName.value = "Error loading subject name" }
         }
     }


    fun loadQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getQuestions(branchId, semesterId, subjectId)
                .onSuccess { questionList ->
                     // Group by year and sort years descending, then sort questions within year by qNumber
                     _questionsByYear.value = questionList
                         .groupBy { it.year }
                         .mapValues { entry ->
                             entry.value.sortedBy { parseQNumber(it.qNumber) } // Sort questions within year
                         }
                         .toSortedMap(compareByDescending { it }) // Sort years descending
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to load questions"
                }
            _isLoading.value = false
        }
    }

    // Helper function to allow sorting Q1a, Q1b, Q2, Q10 etc correctly
    private fun parseQNumber(qNumber: String): Pair<Int, String> {
        val numPart = qNumber.filter { it.isDigit() }
        val letterPart = qNumber.filter { it.isLetter() }
        return Pair(numPart.toIntOrNull() ?: 0, letterPart)
    }
}