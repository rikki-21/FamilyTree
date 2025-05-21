package org.kollarappa.familytree

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.kollarappa.familytree.models.RelationshipType
import org.kollarappa.familytree.ui.personlist.PersonListIntent
import org.kollarappa.familytree.ui.add.PersonListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen() {
    val firestoreService = FirestoreServiceProvider.provideFirestoreService() // Get the platform-specific instance
    val viewModel: PersonListViewModel = viewModel { PersonListViewModel(firestoreService) }
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Clear messages after delay
    LaunchedEffect(state.personAdded, state.relationshipAdded) {
        if (state.personAdded || state.relationshipAdded) {
            delay(3000)
            // Reset success messages
            viewModel.handleIntent(PersonListIntent.ClearMessages)
        }
    }

    LaunchedEffect(Unit) {
        // Force reload persons when screen loads
        viewModel.handleIntent(PersonListIntent.LoadPersons)
    }

    println("check this - Total persons loaded: ${state.allPersons.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
        // Person form card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Person form fields...

                Text("Add Person", style = MaterialTheme.typography.headlineSmall) // Heading
                OutlinedTextField(
                    value = state.personFirstName,
                    onValueChange = {
                        viewModel.handleIntent(
                            PersonListIntent.PersonFirstNameChanged(
                                it
                            )
                        )
                    },
                    label = {
                        Text(
                            "First Name",
                            style = MaterialTheme.typography.labelLarge
                        )
                    } // Label
                )
                OutlinedTextField(
                    value = state.personLastName,
                    onValueChange = {
                        viewModel.handleIntent(
                            PersonListIntent.PersonLastNameChanged(
                                it
                            )
                        )
                    },
                    label = {
                        Text(
                            "Last Name",
                            style = MaterialTheme.typography.labelLarge
                        )
                    } // Label
                )
                OutlinedTextField(
                    value = state.personMiddleName,
                    onValueChange = {
                        viewModel.handleIntent(
                            PersonListIntent.PersonMiddleNameChanged(
                                it
                            )
                        )
                    },
                    label = {
                        Text(
                            "Middle Name",
                            style = MaterialTheme.typography.labelLarge
                        )
                    } // Label
                )
                OutlinedTextField(
                    value = state.personDob,
                    onValueChange = { viewModel.handleIntent(PersonListIntent.PersonAgeChanged(it)) },
                    label = { Text("Age", style = MaterialTheme.typography.labelLarge) } // Label
                )
                // Improve date input with validation
                OutlinedTextField(
                    value = state.personDob,
                    onValueChange = { viewModel.handleIntent(PersonListIntent.PersonAgeChanged(it)) },
                    label = { Text("Birth Date (DD-MM-YYYY)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = { Text("Format: DD-MM-YYYY") },
                    isError = state.personDob.isNotEmpty() && !isValidDateFormat(state.personDob)
                )

//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(
//                        checked = true,
//                        onCheckedChange = {
//                            viewModel.handleIntent(
//                                PersonListIntent.PersonGenderChanged(
//                                    it.toString()
//                                )
//                            )
//                        }
//                    )
//                    Text("Is Alive", style = MaterialTheme.typography.bodyMedium) // Body text
//                }
                Button(
                    onClick = { viewModel.handleIntent(PersonListIntent.AddPerson) },
                    enabled = state.personFirstName.isNotBlank() && state.personLastName.isNotBlank()
                ) {
                    Text("Add Person")
                }

// Display validation errors
                if (state.error != null) {
                    Text(
                        text = state.error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Add Relationship", style = MaterialTheme.typography.headlineSmall) // Heading

        ExposedDropdownMenuBox(
            expanded = state.relationshipPerson1Name.isNotEmpty(),
            onExpandedChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(if (it) "" else state.relationshipPerson1Name)) }
        ) {
            OutlinedTextField(
                value = state.relationshipPerson1Name,
                onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(it)) },
                label = { Text("Person 1 Name", style = MaterialTheme.typography.labelLarge) }, // Label
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.relationshipPerson1Name.isNotEmpty()) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = state.relationshipPerson1Name.isNotEmpty(),
                onDismissRequest = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged("")) }
            ) {
                state.allPersons.map { it.firstName + " " + it.lastName }.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(selectionOption))
                    }, text = {
                        Text(text = selectionOption, style = MaterialTheme.typography.bodyMedium) // Body text
                    })
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = state.relationshipPerson2Name.isNotEmpty(),
            onExpandedChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(if (it) "" else state.relationshipPerson2Name)) }
        ) {
            OutlinedTextField(
                value = state.relationshipPerson2Name,
                onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(it)) },
                label = { Text("Person 2 Name", style = MaterialTheme.typography.labelLarge) }, // Label
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.relationshipPerson2Name.isNotEmpty()) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = state.relationshipPerson2Name.isNotEmpty(),
                onDismissRequest = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged("")) }
            ) {
                state.allPersons.map { it.firstName + " " + it.lastName }.forEach { selectionOption ->
                    DropdownMenuItem(onClick = {
                        viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(selectionOption))
                    }, text =  {
                        Text(text = selectionOption, style = MaterialTheme.typography.bodyMedium) // Body text
                    })
                }
            }
        }

        var expandedRelationship by remember { mutableStateOf(false) }
        Column {
            OutlinedTextField(
                value = state.selectedRelationshipType.name,
                onValueChange = {},
                label = { Text("Relationship Type", style = MaterialTheme.typography.labelLarge) }, // Label
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedRelationship = !expandedRelationship }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                    }
                }
            )
            DropdownMenu(expanded = expandedRelationship, onDismissRequest = { expandedRelationship = false }) {
                RelationshipType.values().forEach { type ->
                    DropdownMenuItem(onClick = {
                        viewModel.handleIntent(PersonListIntent.RelationshipTypeChanged(type))
                        expandedRelationship = false
                    }, text = {
                        Text(text = type.name, style = MaterialTheme.typography.bodyMedium) // Body text
                    })
                }
            }
        }

        OutlinedTextField(
            value = state.relationshipStartDate,
            onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipStartDateChanged(it)) },
            label = { Text("Start Date", style = MaterialTheme.typography.labelLarge) } // Label
        )
        OutlinedTextField(
            value = state.relationshipEndDate,
            onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipEndDateChanged(it)) },
            label = { Text("End Date", style = MaterialTheme.typography.labelLarge) } // Label
        )
        OutlinedTextField(
            value = state.relationshipNotes,
            onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipNotesChanged(it)) },
            label = { Text("Notes", style = MaterialTheme.typography.labelLarge) } // Label
        )

        Button(onClick = {
            viewModel.handleIntent(PersonListIntent.AddRelationship)
        }) {
            Text("Add Relationship", style = MaterialTheme.typography.labelLarge) // Button label
        }

        if (state.personAdded) {
            Text("Person Added", style = MaterialTheme.typography.bodyMedium) // Body text
        }
        if (state.relationshipAdded) {
            Text("Relationship Added", style = MaterialTheme.typography.bodyMedium) // Body text
        }
        if (state.error != null) {
            Text(state.error.toString(), style = MaterialTheme.typography.bodyMedium) // Body text
        }
    }
}

private fun isValidDateFormat(dateStr: String): Boolean {
    val regex = Regex("""^\d{2}-\d{2}-\d{4}$""")
    if (!regex.matches(dateStr)) return false

    try {
        val parts = dateStr.split("-")
        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()

        // Basic validation for month and day ranges
        return year in 1900..2100 && month in 1..12 && day in 1..31
    } catch (e: Exception) {
        return false
    }
}

// Fix gender selection
//Row(verticalAlignment = Alignment.CenterVertically) {
//    Text("Gender:", style = MaterialTheme.typography.bodyMedium)
//    Spacer(modifier = Modifier.width(8.dp))
//
//    val options = listOf("Male", "Female", "Other")
//    options.forEach { gender ->
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = state.gender == gender,
//                onClick = { viewModel.handleIntent(PersonListIntent.PersonGenderChanged(gender)) }
//            )
//            Text(gender)
//        }
//    }
//}