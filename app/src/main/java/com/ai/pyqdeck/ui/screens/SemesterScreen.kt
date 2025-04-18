package com.ai.pyqdeck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ai.pyqdeck.models.Semester
import com.ai.pyqdeck.viewmodels.SemesterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterScreen(
    navController: NavController,
    branchId: String?, // Receive branchId from navigation
    semesterViewModel: SemesterViewModel = viewModel() // Use Hilt or manual factory if needed for SavedStateHandle
) {
    // Note: For SavedStateHandle injection, you might need a ViewModel factory or Hilt.
    // This basic setup assumes the ViewModel can be created directly.
    // If branchId is null, handle appropriately (e.g., show error or navigate back)
    if (branchId == null) {
        // Handle error: Navigate back or show a message
        Text("Error: Branch ID not found.")
        return
    }

    // The ViewModel should ideally be scoped correctly to receive branchId via SavedStateHandle.
    // If not using Hilt/factory, you might need to pass branchId differently or refactor.
    val semesters by semesterViewModel.semesters.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Semester") }, // TODO: Maybe show Branch Name?
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(semesters) { semester ->
                SemesterItem(semester = semester) {
                    // Navigate to Subject screen, passing semesterId
                    navController.navigate("subjectList/${semester.id}")
                }
            }
        }
    }
}

@Composable
fun SemesterItem(semester: Semester, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Add relevant icon or indicator if needed
            Text(text = "Semester ${semester.number}", style = MaterialTheme.typography.titleMedium)
        }
    }
}