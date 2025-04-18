package com.ai.pyqdeck.utils

import android.content.Context
import com.ai.pyqdeck.models.UniversityData
import kotlinx.serialization.json.Json
import java.io.IOException

object DataParser {

    private val json = Json { ignoreUnknownKeys = true } // Configure Json parser

    fun loadUniversityData(context: Context): UniversityData? {
        return try {
            val jsonString = context.assets.open("data.json").bufferedReader().use { it.readText() }
            json.decodeFromString<UniversityData>(jsonString)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null // Handle error appropriately in a real app
        } catch (serializationException: Exception) {
            serializationException.printStackTrace()
            null // Handle parsing error
        }
    }
}