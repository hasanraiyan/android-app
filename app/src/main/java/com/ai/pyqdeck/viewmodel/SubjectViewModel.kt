package com.ai.pyqdeck.viewmodel // Use your package name

import android.app.Application
import androidx.lifecycle.*
import com.ai.pyqdeck.data.DataRepository // Use your package name
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubjectViewModel(
    application: Application,
    private val branchId: String, // Need branch ID to find the correct semester/subject path
    private val semesterId: String // Need semester ID
) : AndroidViewModel(application) {

    private val repository = DataRepository(application)

    // State for the list of subjects
    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    // State for loading status
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State to hold the semester number (optional, but nice for the title)
    private val _semesterNumber = MutableStateFlow<Int?>(null)
    val semesterNumber: StateFlow<Int?> = _semesterNumber.asStateFlow()

    init {
        loadSubjects()
    }

    fun loadSubjects()  {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First, get the semester details to potentially display the number
                val semester = repository.getSemesterById(branchId, semesterId)
                _semesterNumber.value = semester?.number

                // Then, get the subjects for that semester
                _subjects.value = repository.getSubjects(branchId, semesterId)
            } catch (e: Exception) {
                e.printStackTrace()
                _subjects.value = emptyList()
                _semesterNumber.value = null // Reset on error
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Factory for creating ViewModel with arguments ---
    // Place this companion object INSIDE the SubjectViewModel class
    companion object {
        fun provideFactory(
            application: Application,
            branchId: String,
            semesterId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SubjectViewModel::class.java)) {
                    return SubjectViewModel(application, branchId, semesterId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}