// src/main/java/com/ai/pyqdeck/data/DataRepository.kt
package com.ai.pyqdeck.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.IOException

class DataRepository(private val context: Context) {

    private var cachedAppData: AppData? = null

    // Configure Json parser to be lenient
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun getAppData(): Result<AppData> = withContext(Dispatchers.IO) {
        // Return cached data if available
        cachedAppData?.let { return@withContext Result.success(it) }

        try {
            val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
            val appData = json.decodeFromString<AppData>(jsonString)
            cachedAppData = appData // Cache the loaded data
            Result.success(appData)
        } catch (e: IOException) {
            Result.failure(Exception("Error reading data.json from assets", e))
        } catch (e: kotlinx.serialization.SerializationException) {
            Result.failure(Exception("Error parsing data.json", e))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred", e))
        }
    }

    // Helper functions to get specific parts (can be suspend or not depending on caching)
    suspend fun getBranches(): Result<List<Branch>> {
        return getAppData().map { it.branches }
    }

    suspend fun getBranch(branchId: String): Result<Branch?> {
        return getBranches().map { branches -> branches.find { it.id == branchId } }
    }

    suspend fun getSemesters(branchId: String): Result<List<Semester>> {
        return getBranch(branchId).map { it?.semesters ?: emptyList() }
    }

    suspend fun getSemester(branchId: String, semesterId: String): Result<Semester?> {
        return getSemesters(branchId).map { semesters -> semesters.find { it.id == semesterId } }
    }

     suspend fun getSubjects(branchId: String, semesterId: String): Result<List<Subject>> {
        return getSemester(branchId, semesterId).map { it?.subjects ?: emptyList() }
    }

     suspend fun getSubject(branchId: String, semesterId: String, subjectId: String): Result<Subject?> {
        return getSubjects(branchId, semesterId).map { subjects -> subjects.find { it.id == subjectId } }
    }

    suspend fun getQuestions(branchId: String, semesterId: String, subjectId: String): Result<List<Question>> {
         return getSubject(branchId, semesterId, subjectId).map { it?.questions ?: emptyList() }
    }

    // Function to get a specific question (optional, might not be needed if passing Question object)
    suspend fun getQuestion(questionId: String): Result<Question?> {
        return getAppData().map { appData ->
            appData.branches.asSequence()
                .flatMap { it.semesters }
                .flatMap { it.subjects }
                .flatMap { it.questions }
                .find { it.questionId == questionId }
        }
    }
}