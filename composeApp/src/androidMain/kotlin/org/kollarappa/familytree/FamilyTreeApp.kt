package org.kollarappa.familytree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun FamilyTreeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "add") {
        composable("add") {
            Column {
                // Top bar with navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Family Tree App", style = MaterialTheme.typography.headlineMedium)
                    Button(onClick = { navController.navigate("tree") }) {
                        Text("View Tree")
                    }
                }

                // Existing AddScreen content
                AddScreen()
            }
        }

        composable("tree") {
            Column {
                // Top bar with navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { navController.navigateUp() }) {
                        Text("Back")
                    }
                    Text("Family Tree", style = MaterialTheme.typography.headlineMedium)
                }

                // Family tree content
                FamilyTreeScreen()
            }
        }
    }
}