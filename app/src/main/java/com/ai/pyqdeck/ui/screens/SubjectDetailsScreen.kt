package com.ai.pyqdeck.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ai.pyqdeck.models.Module
import com.ai.pyqdeck.models.Question
import com.ai.pyqdeck.viewmodels.SubjectDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailsScreen(
    navController: NavController,
    subjectId: String?, // Receive subjectId from navigation
    subjectDetailsViewModel: SubjectDetailsViewModel = viewModel() // Needs factory/Hilt
) {
    if (subjectId == null) {
        Text("Error: Subject ID not found.")
        return
    }

    val subject by subjectDetailsViewModel.subjectDetails.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subject?.name ?: "Subject Details") },
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
        if (subject == null) {
            // Show loading indicator or error message
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Display Modules
                item {
                    Text("Modules", style = MaterialTheme.typography.headlineSmall)
                }
                items(subject!!.modules) { module ->
                    ModuleItem(module = module)
                }

                // Display Questions (grouped by year, for example)
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Questions", style = MaterialTheme.typography.headlineSmall)
                }
                // Example: Group questions by year
                val questionsByYear = subject!!.questions.groupBy { it.year }.toSortedMap(compareByDescending { it })
                questionsByYear.forEach { (year, questions) ->
                    item {
                        Text("Year: $year", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 8.dp))
                    }
                    items(questions) { question ->
                        QuestionItem(question = question)
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleItem(module: Module) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = module.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = module.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Lectures: ${module.lectures}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QuestionItem(question: Question) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = "${question.qNumber} (${question.marks} Marks)", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = question.text, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Chapter: ${question.chapter}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Type: ${question.type}", style = MaterialTheme.typography.bodySmall)
        }
    }
}