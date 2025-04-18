package com.ai.pyqdeck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.ai.pyqdeck.models.Branch
import com.ai.pyqdeck.viewmodels.BranchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchScreen(
    navController: NavController,
    branchViewModel: BranchViewModel = viewModel()
) {
    val branches by branchViewModel.branches.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Branch") },
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
            if (branches.isEmpty()) {
                item {
                    Text(
                        text = "No branches found. Please check your data.json or Logcat for errors.",
                        modifier = Modifier.fillMaxWidth().padding(32.dp)
                    )
                }
            } else {
                items(branches) { branch ->
                    BranchItem(branch = branch) {
                        // Navigate to Semester screen, passing branchId
                        navController.navigate("semesterList/${branch.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun BranchItem(branch: Branch, onClick: () -> Unit) {
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
            // TODO: Add Icon based on branch.icon if available
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = branch.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}