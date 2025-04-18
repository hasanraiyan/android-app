// src/main/java/com/ai/pyqdeck/MainActivity.kt
package com.ai.pyqdeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.ai.pyqdeck.navigation.AppNavigation
import com.ai.pyqdeck.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                // Remove the default Scaffold and Text here
                // Surface(
                //     modifier = Modifier.fillMaxSize(),
                //     color = MaterialTheme.colorScheme.background
                // ) {
                     AppRoot() // Call a new root composable
                // }
            }
        }
    }
}

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    // Scaffold can be handled within individual screens or here if you want a persistent bottom bar etc.
    // For simplicity, let screens handle their own Scaffold for now.
     AppNavigation(navController = navController, modifier = Modifier.fillMaxSize())

}

// Remove or comment out the Greeting and GreetingPreview functions
// as they are replaced by the navigation structure.

// @Composable
// fun Greeting(name: String, modifier: Modifier = Modifier) { ... }

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() { ... }

// Add a preview for the root or a specific screen if needed
@Preview(showBackground = true)
@Composable
fun AppPreview() {
     MyApplicationTheme {
        // You can preview the AppRoot or a specific screen
        // To preview specific screens requiring ViewModels/Args,
        // you might need to pass dummy data or mock ViewModels.
        // For a simple preview:
        AppRoot()
     }
}