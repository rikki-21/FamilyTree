package org.kollarappa.familytree

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.Relationship
import org.kollarappa.familytree.ui.add.PersonListViewModel
import org.kollarappa.familytree.ui.personlist.PersonListIntent

@Composable
fun FamilyTreeScreen() {
    val firestoreService = FirestoreServiceProvider.provideFirestoreService()
    val viewModel: PersonListViewModel = viewModel { PersonListViewModel(firestoreService) }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(PersonListIntent.LoadPersons)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Family Tree",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (state.allPersons.isEmpty()) {
            Text("No family members found. Add some people first.")
        } else {
            LazyColumn {
                items(state.allPersons) { person ->
                    PersonCard(person, state.allPersons, firestoreService)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun PersonCard(person: Person, allPersons: List<Person>, firestoreService: FirestoreService) {
    var relationships by remember { mutableStateOf<List<Relationship>>(emptyList()) }

    LaunchedEffect(person.id) {
        // Fetch relationships for this person
        relationships = firestoreService.getRelationshipsForPerson(person.id) ?: emptyList()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Person details
            Text(
                "${person.firstName} ${person.lastName}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Birth date: ${person.birthDate ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                "Gender: ${person.gender ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )

            // Relationships section
            if (relationships.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Relationships:", style = MaterialTheme.typography.titleSmall)

                relationships.forEach { relationship ->
                    val relatedPersonId = if (relationship.person1Id == person.id)
                        relationship.person2Id else relationship.person1Id
                    val relatedPerson = allPersons.find { it.id == relatedPersonId }

                    if (relatedPerson != null) {
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "${relationship.relationshipType.name.lowercase().capitalize()} to " +
                                        "${relatedPerson.firstName} ${relatedPerson.lastName}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}