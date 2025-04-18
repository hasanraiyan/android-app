package com.ai.pyqdeck.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.devsrsouza.compose.icons.jetbrains.AllIcons // Import the master icon set
import br.com.devsrsouza.compose.icons.jetbrains.Ionicons // Specific set for lookup
import br.com.devsrsouza.compose.icons.jetbrains.MaterialCommunityIcons // Specific set for lookup
import com.ai.pyqdeck.data.Branch
import com.ai.pyqdeck.data.IconInfo
import com.ai.pyqdeck.viewmodel.BranchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchListScreen(
    onBranchClick: (branchId: String) -> Unit,
    viewModel: BranchViewModel = viewModel() // Get ViewModel instance
) {
    val branches by viewModel.branches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Branch") })
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
                branches.isEmpty() -> {
                     Text(
                         text = "No branches found.",
                         modifier = Modifier.align(Alignment.Center).padding(16.dp)
                     )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(branches, key = { it.id }) { branch ->
                            BranchItem(branch = branch, onClick = { onBranchClick(branch.id) })
                        }
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BranchIcon(iconInfo = branch.icon)
            Text(text = branch.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun BranchIcon(iconInfo: IconInfo, modifier: Modifier = Modifier) {
    // Find the icon based on the set and name
    // You might need a more robust way to handle different icon packs/errors
    val iconVector = when (iconInfo.set.lowercase()) {
        "ionicons" -> Ionicons.AllIcons.find { it.name.equals(iconInfo.name, ignoreCase = true) }
        "materialcommunityicons" -> MaterialCommunityIcons.AllIcons.find { it.name.equals(iconInfo.name, ignoreCase = true) }
        else -> null // Handle unknown sets or provide a default
    }

    if (iconVector != null) {
        Icon(
            imageVector = iconVector,
            contentDescription = iconInfo.name,
            modifier = modifier.size(32.dp), // Adjust size as needed
            tint = MaterialTheme.colorScheme.primary
        )
    } else {
        // Placeholder if icon not found
        Box(modifier.size(32.dp))
    }
}
