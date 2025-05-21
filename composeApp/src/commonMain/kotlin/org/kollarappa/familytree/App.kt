package org.kollarappa.familytree

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.kollarappa.familytree.FirestoreService
import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.Relationship
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
fun App() {
//
//    val firestoreService = remember { FirestoreService() }
//
//    var personFirstName by remember { mutableStateOf("") }
//    var personLastName by remember { mutableStateOf("") }
//    var personDob by remember { mutableStateOf("") }
//    var gender by remember { mutableStateOf("") }
//    var relationshipPerson1Id by remember { mutableStateOf("") }
//    var relationshipPerson2Id by remember { mutableStateOf("") }
//    var relationshipType by remember { mutableStateOf("") }
//    var relationshipStartDate by remember { mutableStateOf("") }
//    var relationshipNotes by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalAlignment = Alignment.Start,
//    ) {
//        // ... (rest of the UI code remains the same)
//        Text("Add Person", style = MaterialTheme.typography.h6)
//        OutlinedTextField(value = personFirstName, onValueChange = { personFirstName = it }, label = { Text("First Name") })
//        OutlinedTextField(value = personLastName, onValueChange = { personLastName = it }, label = { Text("Last Name") })
//        OutlinedTextField(value = personDob, onValueChange = { personDob = it }, label = { Text("DD-MM-YYYY") })
//        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("gender") })
////        Row(verticalAlignment = Alignment.CenterVertically) {
////            Checkbox(checked = personIsAlive, onCheckedChange = { personIsAlive = it })
////            Text("Is Alive")
////        }
//        Button(onClick = {
//            coroutineScope.launch {
//                firestoreService.addPerson(
//                    Person(
//                        id = Uuid.random().toString(),
//                        firstName = personFirstName,
//                        lastName = personLastName,
//                        birthDate = personDob.takeIf { it.isNotEmpty() },
//                        gender = gender ?: "",
//                    )
//                )
//                personFirstName = ""
//                personLastName = ""
//                personDob = ""
//                gender = ""
//            }
//        }) {
//            Text("Add Person")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Add Relationship", style = MaterialTheme.typography.h6)
//        OutlinedTextField(value = relationshipPerson1Id, onValueChange = { relationshipPerson1Id = it }, label = { Text("Person 1 ID") })
//        OutlinedTextField(value = relationshipPerson2Id, onValueChange = { relationshipPerson2Id = it }, label = { Text("Person 2 ID") })
//        OutlinedTextField(value = relationshipType, onValueChange = { relationshipType = it }, label = { Text("Relationship Type") })
//        OutlinedTextField(value = relationshipStartDate, onValueChange = { relationshipStartDate = it }, label = { Text("Start Date") })
//        OutlinedTextField(value = relationshipNotes, onValueChange = { relationshipNotes = it }, label = { Text("Notes") })
//        Button(onClick = {
//            coroutineScope.launch {
//                firestoreService.addRelationship(
//                    Relationship(
//                        id = Uuid.random().toString(),
//                        person1Id = relationshipPerson1Id,
//                        person2Id = relationshipPerson2Id,
//                        relationshipType = relationshipType,
//                        startDate = relationshipStartDate,
//                        notes = relationshipNotes.takeIf { it.isNotEmpty() },
//                    )
//                )
//                relationshipPerson1Id = ""
//                relationshipPerson2Id = ""
//                relationshipType = ""
//                relationshipStartDate = ""
//                relationshipNotes = ""
//            }
//        }) {
//            Text("Add Relationship")
//        }
//    }
}
//
//package org.kollarappa.familytree.ui
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.launch
//import org.kollarappa.familytree.FirestoreService
//import org.kollarappa.familytree.models.Person
//import org.kollarappa.familytree.models.Relationship
//import io.github.benmanes.caffeine.uuid.UUID
//
//@Composable
//fun FamilyTreeApp() {
//    val firestoreService = remember { FirestoreService() }
//
//    var personFirstName by remember { mutableStateOf("") }
//    var personLastName by remember { mutableStateOf("") }
//    var personMiddleName by remember { mutableStateOf("") }
//    var personAge by remember { mutableStateOf("") }
//    var personIsAlive by remember { mutableStateOf(true) }
//
//    var relationshipPerson1Name by remember { mutableStateOf("") }
//    var relationshipPerson2Name by remember { mutableStateOf("") }
//    var selectedRelationshipType by remember { mutableStateOf(RelationshipType.HUSBAND) }
//    var relationshipStartDate by remember { mutableStateOf("") }
//    var relationshipEndDate by remember { mutableStateOf("") }
//    var relationshipNotes by remember { mutableStateOf("") }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    val scrollState = rememberScrollState()
//
//    val allPersons by firestoreService.getAllPersonsFlow().collectAsState(initial = emptyList())
//    val personNames = allPersons.map { it.firstName + " " + it.lastName }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .verticalScroll(scrollState),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text("Add Person", style = MaterialTheme.typography.h6)
//        OutlinedTextField(value = personFirstName, onValueChange = { personFirstName = it }, label = { Text("First Name") })
//        OutlinedTextField(value = personLastName, onValueChange = { personLastName = it }, label = { Text("Last Name") })
//        OutlinedTextField(value = personMiddleName, onValueChange = { personMiddleName = it }, label = { Text("Middle Name") })
//        OutlinedTextField(value = personAge, onValueChange = { personAge = it }, label = { Text("Age") })
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(checked = personIsAlive, onCheckedChange = { personIsAlive = it })
//            Text("Is Alive")
//        }
//        Button(onClick = {
//            coroutineScope.launch {
//                firestoreService.addPerson(
//                    Person(
//                        id = UUID.randomUUID().toString(),
//                        firstName = personFirstName,
//                        lastName = personLastName,
//                        middleName = personMiddleName.takeIf { it.isNotEmpty() },
//                        age = personAge.toIntOrNull() ?: 0,
//                        isAlive = personIsAlive,
//                    )
//                )
//                personFirstName = ""
//                personLastName = ""
//                personMiddleName = ""
//                personAge = ""
//                personIsAlive = true
//            }
//        }) {
//            Text("Add Person")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Add Relationship", style = MaterialTheme.typography.h6)
//
//        ExposedDropdownMenuBox(
//            expanded = relationshipPerson1Name.isNotEmpty(),
//            onExpandedChange = { relationshipPerson1Name = if (it) "" else relationshipPerson1Name }
//        ) {
//            OutlinedTextField(
//                value = relationshipPerson1Name,
//                onValueChange = { relationshipPerson1Name = it },
//                label = { Text("Person 1 Name") },
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = relationshipPerson1Name.isNotEmpty()) },
//                modifier = Modifier.menuAnchor()
//            )
//            ExposedDropdownMenu(
//                expanded = relationshipPerson1Name.isNotEmpty(),
//                onDismissRequest = { relationshipPerson1Name = "" }
//            ) {
//                personNames.forEach { selectionOption ->
//                    DropdownMenuItem(onClick = {
//                        relationshipPerson1Name = selectionOption
//                    }) {
//                        Text(text = selectionOption)
//                    }
//                }
//            }
//        }
//
//        ExposedDropdownMenuBox(
//            expanded = relationshipPerson2Name.isNotEmpty(),
//            onExpandedChange = { relationshipPerson2Name = if (it) "" else relationshipPerson2Name }
//        ) {
//            OutlinedTextField(
//                value = relationshipPerson2Name,
//                onValueChange = { relationshipPerson2Name = it },
//                label = { Text("Person 2 Name") },
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = relationshipPerson2Name.isNotEmpty()) },
//                modifier = Modifier.menuAnchor()
//            )
//            ExposedDropdownMenu(
//                expanded = relationshipPerson2Name.isNotEmpty(),
//                onDismissRequest = { relationshipPerson2Name = "" }
//            ) {
//                personNames.forEach { selectionOption ->
//                    DropdownMenuItem(onClick = {
//                        relationshipPerson2Name = selectionOption
//                    }) {
//                        Text(text = selectionOption)
//                    }
//                }
//            }
//        }
//
//        var expandedRelationship by remember { mutableStateOf(false) }
//        Column {
//            OutlinedTextField(
//                value = selectedRelationshipType.name,
//                onValueChange = {},
//                label = { Text("Relationship Type") },
//                readOnly = true,
//                trailingIcon = {
//                    IconButton(onClick = { expandedRelationship = !expandedRelationship }) {
//                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
//                    }
//                }
//            )
//            DropdownMenu(expanded = expandedRelationship, onDismissRequest = { expandedRelationship = false }) {
//                RelationshipType.values().forEach { type ->
//                    DropdownMenuItem(onClick = {
//                        selectedRelationshipType = type
//                        expandedRelationship = false
//                    }) {
//                        Text(text = type.name)
//                    }
//                }
//            }
//        }
//
//        OutlinedTextField(value = relationshipStartDate, onValueChange = { relationshipStartDate = it }, label = { Text("Start Date") })
//        OutlinedTextField(value = relationshipEndDate, onValueChange = { relationshipEndDate = it }, label = { Text("End Date") })
//        OutlinedTextField(value = relationshipNotes, onValueChange = { relationshipNotes = it }, label = { Text("Notes") })
//
//        Button(onClick = {
//            coroutineScope.launch {
//                val person1 = allPersons.find { it.firstName + " " + it.lastName == relationshipPerson1Name }
//                val person2 = allPersons.find { it.firstName + " " + it.lastName == relationshipPerson2Name }
//
//                if (person1 != null && person2 != null) {
//                    firestoreService.addRelationship(
//                        Relationship(
//                            id = UUID.randomUUID().toString(),
//                            person1Id = person1.id,
//                            person2Id = person2.id,
//                            relationshipType = selectedRelationshipType,
//                            startDate = relationshipStartDate,
//                            endDate = relationshipEndDate.takeIf { it.isNotEmpty() },
//                            notes = relationshipNotes.takeIf { it.isNotEmpty() },
//                        )
//                    )
//                    relationshipPerson1Name = ""
//                    relationshipPerson2Name = ""
//                    relationshipStartDate = ""
//                    relationshipEndDate = ""
//                    relationshipNotes = ""
//                } else {
//                    // Handle case where person names are not found
//                    println("Person names not found.")
//                }
//            }
//        }) {
//            Text("Add Relationship")
//        }
//    }
//}
