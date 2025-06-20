package org.kollarappa.familytree

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
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
//
//        ExposedDropdownMenuBox(
//            expanded = state.relationshipPerson1Name.isNotEmpty(),
//            onExpandedChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(if (it) "" else state.relationshipPerson1Name)) }
//        ) {
//            OutlinedTextField(
//                value = state.relationshipPerson1Name,
//                onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(it)) },
//                label = { Text("Person 1 Name", style = MaterialTheme.typography.labelLarge) }, // Label
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.relationshipPerson1Name.isNotEmpty()) },
//                modifier = Modifier.menuAnchor()
//            )
//            ExposedDropdownMenu(
//                expanded = state.relationshipPerson1Name.isNotEmpty(),
//                onDismissRequest = { viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged("")) }
//            ) {
//                state.allPersons.map { it.firstName + " " + it.lastName }.forEach { selectionOption ->
//                    DropdownMenuItem(onClick = {
//                        viewModel.handleIntent(PersonListIntent.RelationshipPerson1NameChanged(selectionOption))
//                    }, text = {
//                        Text(text = selectionOption, style = MaterialTheme.typography.bodyMedium) // Body text
//                    })
//                }
//            }
//        }
//
//        ExposedDropdownMenuBox(
//            expanded = state.relationshipPerson2Name.isNotEmpty(),
//            onExpandedChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(if (it) "" else state.relationshipPerson2Name)) }
//        ) {
//            OutlinedTextField(
//                value = state.relationshipPerson2Name,
//                onValueChange = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(it)) },
//                label = { Text("Person 2 Name", style = MaterialTheme.typography.labelLarge) }, // Label
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.relationshipPerson2Name.isNotEmpty()) },
//                modifier = Modifier.menuAnchor()
//            )
//            ExposedDropdownMenu(
//                expanded = state.relationshipPerson2Name.isNotEmpty(),
//                onDismissRequest = { viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged("")) }
//            ) {
//                state.allPersons.map { it.firstName + " " + it.lastName }.forEach { selectionOption ->
//                    DropdownMenuItem(onClick = {
//                        viewModel.handleIntent(PersonListIntent.RelationshipPerson2NameChanged(selectionOption))
//                    }, text =  {
//                        Text(text = selectionOption, style = MaterialTheme.typography.bodyMedium) // Body text
//                    })
//                }
//            }
//        }

//        var person1DropdownExpanded by remember { mutableStateOf(false) }
//
//        Column { // Wrap TextField and DropdownMenu
//            OutlinedTextField(
//                value = state.selectedPerson1?.let { "${it.firstName} ${it.lastName}" } ?: state.person1SearchQuery,
//                onValueChange = { query ->
//                    viewModel.handleIntent(PersonListIntent.SearchPerson1(query))
//                    person1DropdownExpanded = query.isNotBlank() && state.person1SearchResults.isNotEmpty()
//                },
//                label = { Text("Search Person 1") },
//                modifier = Modifier.fillMaxWidth(),
//                trailingIcon = {
//                    if (state.selectedPerson1 != null || state.person1SearchQuery.isNotEmpty()) {
//                        IconButton(onClick = {
//                            viewModel.handleIntent(PersonListIntent.ClearPerson1Selection) // Implement this intent
//                            viewModel.handleIntent(PersonListIntent.SearchPerson1("")) // Clear search results
//                            person1DropdownExpanded = false
//                        }) {
//                            Icon(Icons.Filled.Clear, "Clear selection")
//                        }
//                    }
//                }
//            )
//
//            if (state.isSearchingPerson1) { // Add this to state
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//            }
//
//            DropdownMenu(
//                expanded = person1DropdownExpanded && state.person1SearchResults.isNotEmpty(),
//                onDismissRequest = { person1DropdownExpanded = false },
//                modifier = Modifier.fillMaxWidth() // Adjust width as needed
//            ) {
//                state.person1SearchResults.forEach { person ->
//                    DropdownMenuItem(
//                        text = { Text("${person.firstName} ${person.lastName}") },
//                        onClick = {
//                            viewModel.handleIntent(PersonListIntent.SelectPerson1(person))
//                            person1DropdownExpanded = false
//                        }
//                    )
//                }
//                if (state.person1SearchResults.isEmpty() && state.person1SearchQuery.isNotBlank() && !state.isSearchingPerson1) {
//                    DropdownMenuItem(
//                        text = { Text("No results found") },
//                        onClick = { person1DropdownExpanded = false },
//                        enabled = false
//                    )
//                }
//            }
//        }

        var person1DropdownExpanded by remember { mutableStateOf(false) }

        Column { // Wrap TextField and DropdownMenu
            OutlinedTextField(
                value = state.selectedPerson1?.let { "${it.firstName} ${it.lastName}" } ?: state.person1SearchQuery,
                onValueChange = { query ->
                    viewModel.handleIntent(PersonListIntent.SearchPerson1(query))
                    // Expand dropdown if user is typing a non-blank query
                    person1DropdownExpanded = query.isNotBlank()
                },
                label = { Text("Search Person 1") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (state.selectedPerson1 != null || state.person1SearchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.handleIntent(PersonListIntent.ClearPerson1Selection)
                            person1DropdownExpanded = false // Close dropdown
                        }) {
                            Icon(Icons.Filled.Clear, "Clear selection or search")
                        }
                    }
                }
            )

            if (state.isSearchingPerson1) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            }

            // DropdownMenu for Person 1 search results
            // Expanded if:
            // 1. person1DropdownExpanded is true (user interacted with field)
            // 2. There's an active search query (state.person1SearchQuery.isNotBlank())
            // 3. We are not currently in the process of searching (state.isSearchingPerson1 is false)
            val showPerson1Dropdown = person1DropdownExpanded && state.person1SearchQuery.isNotBlank() && !state.isSearchingPerson1

            DropdownMenu(
                expanded = showPerson1Dropdown,
                onDismissRequest = { person1DropdownExpanded = false },
                modifier = Modifier.fillMaxWidth() // Adjust width as needed
            ) {
                // This content is rendered only if 'expanded' (showPerson1Dropdown) is true.
                // So, state.person1SearchQuery.isNotBlank() and !state.isSearchingPerson1 are true here.
                if (state.person1SearchResults.isNotEmpty()) {
                    state.person1SearchResults.forEach { person ->
                        DropdownMenuItem(
                            text = { Text("${person.firstName} ${person.lastName}") },
                            onClick = {
                                viewModel.handleIntent(PersonListIntent.SelectPerson1(person))
                                person1DropdownExpanded = false // Close dropdown after selection
                            }
                        )
                    }
                } else { // No results, but person1SearchQuery is not blank and not currently searching
                    DropdownMenuItem(
                        text = { Text("No results found for \"${state.person1SearchQuery}\"") },
                        onClick = { person1DropdownExpanded = false }, // Close dropdown
                        enabled = false
                    )
                }
            }
        }
        // Person 2 search dropdown
        var person2DropdownExpanded by remember { mutableStateOf(false) }

        Column { // Wrap TextField and DropdownMenu
            OutlinedTextField(
                value = state.selectedPerson2?.let { "${it.firstName} ${it.lastName}" } ?: state.person2SearchQuery,
                onValueChange = { query ->
                    viewModel.handleIntent(PersonListIntent.SearchPerson2(query))
                    // Expand dropdown if user is typing a non-blank query
                    person2DropdownExpanded = query.isNotBlank()
                },
                label = { Text("Search Person 2") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (state.selectedPerson2 != null || state.person2SearchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.handleIntent(PersonListIntent.ClearPerson2Selection)
                            person2DropdownExpanded = false // Close dropdown
                        }) {
                            Icon(Icons.Filled.Clear, "Clear selection or search")
                        }
                    }
                }
            )

            if (state.isSearchingPerson2) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            }

            // DropdownMenu for Person 2 search results
            // Expanded if:
            // 1. person1DropdownExpanded is true (user interacted with field)
            // 2. There's an active search query (state.person1SearchQuery.isNotBlank())
            // 3. We are not currently in the process of searching (state.isSearchingPerson1 is false)
            val showPerson2Dropdown = person2DropdownExpanded && state.person2SearchQuery.isNotBlank() && !state.isSearchingPerson2

            DropdownMenu(
                expanded = showPerson2Dropdown,
                onDismissRequest = { person2DropdownExpanded = false },
                modifier = Modifier.fillMaxWidth() // Adjust width as needed
            ) {
                // This content is rendered only if 'expanded' (showPerson2Dropdown) is true.
                // So, state.person2SearchQuery.isNotBlank() and !state.isSearchingPerson2 are true here.
                if (state.person2SearchResults.isNotEmpty()) {
                    state.person2SearchResults.forEach { person ->
                        DropdownMenuItem(
                            text = { Text("${person.firstName} ${person.lastName}") },
                            onClick = {
                                viewModel.handleIntent(PersonListIntent.SelectPerson2(person))
                                person2DropdownExpanded = false // Close dropdown after selection
                            }
                        )
                    }
                } else { // No results, but person2SearchQuery is not blank and not currently searching
                    DropdownMenuItem(
                        text = { Text("No results found for \"${state.person2SearchQuery}\"") },
                        onClick = { person2DropdownExpanded = false }, // Close dropdown
                        enabled = false
                    )
                }
            }
        }

//        var person2DropdownExpanded by remember { mutableStateOf(false) }
//
//        Column { // Wrap TextField and DropdownMenu
//            OutlinedTextField(
//                value = state.selectedPerson2?.let { "${it.firstName} ${it.lastName}" } ?: state.person2SearchQuery,
//                onValueChange = { query ->
//                    viewModel.handleIntent(PersonListIntent.SearchPerson2(query))
//                    person2DropdownExpanded = query.isNotBlank() && state.person2SearchResults.isNotEmpty()
//                },
//                label = { Text("Search Person 2") },
//                modifier = Modifier.fillMaxWidth(),
//                trailingIcon = {
//                    if (state.selectedPerson2 != null || state.person2SearchQuery.isNotEmpty()) {
//                        IconButton(onClick = {
//                            viewModel.handleIntent(PersonListIntent.ClearPerson2Selection) // Implement this intent
//                            viewModel.handleIntent(PersonListIntent.SearchPerson2("")) // Clear search results
//                            person2DropdownExpanded = false
//                        }) {
//                            Icon(Icons.Filled.Clear, "Clear selection")
//                        }
//                    }
//                }
//            )
//
//            if (state.isSearchingPerson2) { // Add this to state
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//            }
//
//            DropdownMenu(
//                expanded = person2DropdownExpanded && state.person2SearchResults.isNotEmpty(),
//                onDismissRequest = { person2DropdownExpanded = false },
//                modifier = Modifier.fillMaxWidth() // Adjust width as needed
//            ) {
//                state.person2SearchResults.forEach { person ->
//                    DropdownMenuItem(
//                        text = { Text("${person.firstName} ${person.lastName}") },
//                        onClick = {
//                            viewModel.handleIntent(PersonListIntent.SelectPerson2(person))
//                            person2DropdownExpanded = false
//                        }
//                    )
//                }
//                if (state.person2SearchResults.isEmpty() && state.person2SearchQuery.isNotBlank() && !state.isSearchingPerson2) {
//                    DropdownMenuItem(
//                        text = { Text("No results found") },
//                        onClick = { person2DropdownExpanded = false },
//                        enabled = false
//                    )
//                }
//            }
//        }

        var relationshipTypeExpanded by remember { mutableStateOf(false) }
        val relationshipTypes = remember { RelationshipType.values() } // Get all enum values

        ExposedDropdownMenuBox(
            expanded = relationshipTypeExpanded,
            onExpandedChange = { relationshipTypeExpanded = !relationshipTypeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.selectedRelationshipType.displayName,
                onValueChange = {}, // Not directly editable
                readOnly = true,
                label = { Text("Relationship Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = relationshipTypeExpanded) },
                modifier = Modifier
                    .menuAnchor() // Important for ExposedDropdownMenuBox
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = relationshipTypeExpanded,
                onDismissRequest = { relationshipTypeExpanded = false }
            ) {
                relationshipTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.displayName) }, // Assuming RelationshipType has a user-friendly displayName
                        onClick = {
                            viewModel.handleIntent(PersonListIntent.RelationshipTypeChanged(type))
                            relationshipTypeExpanded = false
                        }
                    )
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