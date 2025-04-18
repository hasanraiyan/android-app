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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ai.pyqdeck.data.Question
import com.ai.pyqdeck.viewmodel.QuestionListViewModel
// If using Markdown: import com.halilibo.compose.markdown.Markdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionListScreen(
     onBackClick: () -> Unit,
     viewModel: QuestionListViewModel = viewModel()
     // Add onQuestionClick lambda if you need a detail screen
) {
     val questions by viewModel.questionsByYear.collectAsState() // Grouped by year
     val isLoading by viewModel.isLoading.collectAsState()
     val error by viewModel.error.collectAsState()
     val subjectName by viewModel.subjectName.collectAsState()

     Scaffold(
         topBar = {
             TopAppBar(
                 title = { Text(subjectName.ifEmpty { "Questions" }) },
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
                 questions.isEmpty() && !isLoading -> {
                     Text(
                         text = "No questions found for this subject.",
                         modifier = Modifier.align(Alignment.Center).padding(16.dp)
                     )
                 }
                 else -> {
                     LazyColumn(
                         modifier = Modifier.fillMaxSize(),
                         contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                         verticalArrangement = Arrangement.spacedBy(16.dp)
                     ) {
                         questions.forEach { (year, questionList) ->
                             item {
                                 Text(
                                     text = "Year: $year",
                                     style = MaterialTheme.typography.headlineSmall,
                                     modifier = Modifier.padding(vertical = 8.dp)
                                 )
                             }
                             items(questionList, key = { it.questionId }) { question ->
                                 QuestionItem(question = question)
                             }
                             item { Divider(modifier = Modifier.padding(vertical = 8.dp)) } // Divider between years
                         }
                     }
                 }
             }
         }
     }
}

@Composable
fun QuestionItem(question: Question) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question.qNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${question.marks} Marks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Module: ${question.chapter}",
                style = MaterialTheme.typography.labelSmall,
                 color = MaterialTheme.colorScheme.outline
            )
             Spacer(modifier = Modifier.height(4.dp))
             Text(
                 text = "Type: ${question.type}",
                 style = MaterialTheme.typography.labelSmall,
                 color = MaterialTheme.colorScheme.outline
             )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Display Question Text ---
            // Option 1: Simple Text
            Text(text = question.text, style = MaterialTheme.typography.bodyLarge)

            // Option 2: Using a Markdown library (if added dependency)
            // Remember to handle potential image links within markdown if necessary
            // Markdown(content = question.text, modifier = Modifier.fillMaxWidth())

        }
    }
}