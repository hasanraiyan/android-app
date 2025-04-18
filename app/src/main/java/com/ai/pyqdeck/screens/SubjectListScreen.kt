package com.ai.pyqdeck.screens 

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ai.pyqdeck.data.Subject // Use your package name
import com.ai.pyqdeck.navigation.Screen // Use your package name
import com.ai.pyqdeck.viewmodel.SubjectViewModel // Use your package name

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListScreen(
    navController: NavHostController,
    branchId: String,
    semesterId: String
) {
    val application = LocalContext.current.applicationContext as Application
    // Create the ViewModel using its factory
    val viewModel: SubjectViewModel = viewModel(
        factory = SubjectViewModel.provideFactory(application, branchId, semesterId)
    )

    // Observe state from the ViewModel
    val subjects by viewModel.subjects.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val semesterNumber by viewModel.semesterNumber.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Semester ${semesterNumber ?: "..."} Subjects") }, // Show semester number if available
                // Add back navigation if needed
                // navigationIcon = { ... }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (subjects.isEmpty()) {
                Text("No subjects found for this semester.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(subjects) { subject ->
                        SubjectItem(subject = subject) {
                            // Navigate to Subject Detail Screen on click
                            navController.navigate(
                                Screen.SubjectDetail.createRoute(
                                    branchId = branchId,
                                    semesterId = semesterId,
                                    subjectId = subject.id
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectItem(subject: Subject, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Code: ${subject.code}",
                style = MaterialTheme.typography.bodyMedium
            )
            // Optionally display number of modules/questions
            // Text("Modules: ${subject.modules.size}", style = MaterialTheme.typography.bodySmall)
            // Text("Questions: ${subject.questions.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}