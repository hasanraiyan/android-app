// src/main/java/com/ai/pyqdeck/viewmodel/BranchViewModel.kt
package com.ai.pyqdeck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.data.Branch
import com.ai.pyqdeck.data.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BranchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataRepository(application)

    private val _branches = MutableStateFlow<List<Branch>>(emptyList())
    val branches: StateFlow<List<Branch>> = _branches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadBranches()
    }

    fun loadBranches() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getBranches()
                .onSuccess { branchList ->
                    _branches.value = branchList
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to load branches"
                }
            _isLoading.value = false
        }
    }
}


// Create similar ViewModels for Semesters, Subjects, Questions as needed
// Example: SemesterViewModel
// src/main/java/com/ai/pyqdeck/viewmodel/SemesterViewModel.kt
package com.ai.pyqdeck.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ai.pyqdeck.data.DataRepository
import com.ai.pyqdeck.data.Semester
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SemesterViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = DataRepository(application)
    val branchId: String = savedStateHandle.get<String>("branchId") ?: "" // Get branchId from navigation args

    private val _semesters = MutableStateFlow<List<Semester>>(emptyList())
    val semesters: StateFlow<List<Semester>> = _semesters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

     private val _branchName = MutableStateFlow<String>("")
     val branchName: StateFlow<String> = _branchName.asStateFlow()


    init {
        if (branchId.isNotEmpty()) {
            loadSemesters()
            loadBranchName()
        } else {
             _error.value = "Branch ID not provided"
        }
    }

     private fun loadBranchName() {
         viewModelScope.launch {
             repository.getBranch(branchId)
                 .onSuccess { branch -> _branchName.value = branch?.name ?: "Unknown Branch"}
                 .onFailure { _branchName.value = "Error loading branch name" }
         }
     }


    fun loadSemesters() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository.getSemesters(branchId)
                .onSuccess { semesterList ->
                    _semesters.value = semesterList.sortedBy { it.number } // Sort by semester number
                }
                .onFailure { throwable ->
                    _error.value = throwable.message ?: "Failed to load semesters"
                }
            _isLoading.value = false
        }
    }
}

// Create SubjectViewModel and QuestionListViewModel similarly, accepting necessary IDs
// from SavedStateHandle