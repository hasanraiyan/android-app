package com.ai.pyqdeck.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Import screen composables once they are created
import com.ai.pyqdeck.ui.screens.BranchScreen
import com.ai.pyqdeck.ui.screens.SemesterScreen
import com.ai.pyqdeck.ui.screens.SubjectScreen
import com.ai.pyqdeck.ui.screens.SubjectDetailsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "branchList") {
        composable("branchList") {
            BranchScreen(navController = navController)
        }
        // Define other destinations (semesters, subjects, details) here
        composable("semesterList/{branchId}") { backStackEntry ->
            val branchId = backStackEntry.arguments?.getString("branchId")
            SemesterScreen(navController = navController, branchId = branchId)
        }
        composable("subjectList/{semesterId}") { backStackEntry ->
            val semesterId = backStackEntry.arguments?.getString("semesterId")
            SubjectScreen(navController = navController, semesterId = semesterId)
        }
        composable("subjectDetails/{subjectId}") { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("subjectId")
            SubjectDetailsScreen(navController = navController, subjectId = subjectId)
        }
    }
}