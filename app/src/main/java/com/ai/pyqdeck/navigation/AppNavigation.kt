// src/main/java/com/ai/pyqdeck/navigation/AppNavigation.kt
package com.ai.pyqdeck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ai.pyqdeck.ui.screens.BranchListScreen
import com.ai.pyqdeck.ui.screens.QuestionListScreen
import com.ai.pyqdeck.ui.screens.SemesterListScreen
import com.ai.pyqdeck.ui.screens.SubjectListScreen
// Import your Screen composables

object Destinations {
    const val BRANCH_LIST = "branchList"
    const val SEMESTER_LIST = "semesterList/{branchId}" // Use path parameter
    const val SUBJECT_LIST = "subjectList/{branchId}/{semesterId}"
    const val QUESTION_LIST = "questionList/{branchId}/{semesterId}/{subjectId}"

    // Helper functions to create routes with arguments
    fun semesterListRoute(branchId: String) = "semesterList/$branchId"
    fun subjectListRoute(branchId: String, semesterId: String) = "subjectList/$branchId/$semesterId"
    fun questionListRoute(branchId: String, semesterId: String, subjectId: String) = "questionList/$branchId/$semesterId/$subjectId"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.BRANCH_LIST,
        modifier = modifier
    ) {
        composable(Destinations.BRANCH_LIST) {
            BranchListScreen(
                onBranchClick = { branchId ->
                    navController.navigate(Destinations.semesterListRoute(branchId))
                }
            )
        }

        composable(
            route = Destinations.SEMESTER_LIST,
            arguments = listOf(navArgument("branchId") { type = NavType.StringType })
        ) { backStackEntry ->
            // ViewModel will get branchId from SavedStateHandle
            SemesterListScreen(
                onSemesterClick = { branchId, semesterId ->
                     navController.navigate(Destinations.subjectListRoute(branchId, semesterId))
                },
                 onBackClick = { navController.popBackStack() }
            )
        }

         composable(
             route = Destinations.SUBJECT_LIST,
             arguments = listOf(
                 navArgument("branchId") { type = NavType.StringType },
                 navArgument("semesterId") { type = NavType.StringType }
             )
         ) { backStackEntry ->
             SubjectListScreen(
                 onSubjectClick = { branchId, semesterId, subjectId ->
                    navController.navigate(Destinations.questionListRoute(branchId, semesterId, subjectId))
                 },
                 onBackClick = { navController.popBackStack() }
                 // ViewModel gets IDs from SavedStateHandle
             )
         }

         composable(
             route = Destinations.QUESTION_LIST,
             arguments = listOf(
                 navArgument("branchId") { type = NavType.StringType },
                 navArgument("semesterId") { type = NavType.StringType },
                 navArgument("subjectId") { type = NavType.StringType }
             )
         ) { backStackEntry ->
             QuestionListScreen(
                 onBackClick = { navController.popBackStack() }
                 // ViewModel gets IDs from SavedStateHandle
                 // Potentially add onQuestionClick for detail view later
             )
         }

        // Add composables for other screens (SubjectList, QuestionList, QuestionDetail)
        // following the same pattern
    }
}