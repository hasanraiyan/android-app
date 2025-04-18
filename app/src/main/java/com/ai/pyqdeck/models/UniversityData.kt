package com.ai.pyqdeck.models

import kotlinx.serialization.Serializable

@Serializable
data class UniversityData(
    val branches: List<Branch>
)

@Serializable
data class Branch(
    val id: String,
    val name: String,
    val icon: Icon? = null, // Make icon optional or provide a default
    val semesters: List<Semester>
)

@Serializable
data class Icon(
    val set: String,
    val name: String
)

@Serializable
data class Semester(
    val id: String,
    val number: Int,
    val subjects: List<Subject>
)

@Serializable
data class Subject(
    val id: String,
    val name: String,
    val code: String,
    val modules: List<Module>,
    val questions: List<Question>
)

@Serializable
data class Module(
    val id: String,
    val name: String,
    val description: String,
    val lectures: Int
)

@Serializable
data class Question(
    val questionId: String,
    val year: Int,
    val qNumber: String,
    val chapter: String,
    val text: String,
    val type: String,
    val marks: Double
)