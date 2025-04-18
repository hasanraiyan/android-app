package com.ai.pyqdeck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ai.pyqdeck.data.Semester
import com.ai.pyqdeck.viewmodel.SemesterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterListScreen(
    onSemesterClick: (branchId: String, semesterId: String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SemesterViewModel = viewModel()
) {
    val semesters by viewModel.semesters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val branchName by viewModel.branchName.collectAsState()
    val branchId = viewModel.branchId // Get from ViewModel


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(branchName.ifEmpty { "Select Semester" }) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center).padding(16.dp)
                    )
                }
                 semesters.isEmpty() && !isLoading -> {
                     Text(
                         text = "No semesters found for this branch.",
                         modifier = Modifier.align(Alignment.Center).padding(16.dp)
                     )
                 }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(semesters, key = { it.id }) { semester ->
                            SemesterItem(
                                semester = semester,
                                onClick = { onSemesterClick(branchId, semester.id) }
                            )
                        }
                    }
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
            Text(text = "Semester ${semester.number}", style = MaterialTheme.typography.titleMedium)
        }
    }
}