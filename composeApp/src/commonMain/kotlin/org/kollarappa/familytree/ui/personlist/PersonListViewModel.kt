package org.kollarappa.familytree.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kollarappa.familytree.FirestoreService
import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.Relationship
import org.kollarappa.familytree.ui.personlist.PersonListIntent
import org.kollarappa.familytree.ui.personlist.PersonListState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class PersonListViewModel(private val firestoreService: FirestoreService) : ViewModel() {

    private val _state = MutableStateFlow(PersonListState())
    val state: StateFlow<PersonListState> = _state

    init {
        handleIntent(PersonListIntent.LoadPersons)
    }

    fun handleIntent(intent: PersonListIntent) {
        when (intent) {
            is PersonListIntent.PersonFirstNameChanged -> _state.value = _state.value.copy(personFirstName = intent.name)
            is PersonListIntent.PersonLastNameChanged -> _state.value = _state.value.copy(personLastName = intent.name)
            is PersonListIntent.PersonMiddleNameChanged -> _state.value = _state.value.copy(personMiddleName = intent.name)
            is PersonListIntent.PersonAgeChanged -> _state.value = _state.value.copy(personDob = intent.age)
            is PersonListIntent.PersonGenderChanged -> _state.value = _state.value.copy(gender = intent.gender)
            is PersonListIntent.RelationshipPerson1NameChanged -> _state.value = _state.value.copy(relationshipPerson1Name = intent.name)
            is PersonListIntent.RelationshipPerson2NameChanged -> _state.value = _state.value.copy(relationshipPerson2Name = intent.name)
            is PersonListIntent.RelationshipTypeChanged -> _state.value = _state.value.copy(selectedRelationshipType = intent.type)
            is PersonListIntent.RelationshipStartDateChanged -> _state.value = _state.value.copy(relationshipStartDate = intent.date)
            is PersonListIntent.RelationshipEndDateChanged -> _state.value = _state.value.copy(relationshipEndDate = intent.date)
            is PersonListIntent.RelationshipNotesChanged -> _state.value = _state.value.copy(relationshipNotes = intent.notes)
            is PersonListIntent.ClearMessages -> _state.value = _state.value.copy(
                personAdded = false,
                relationshipAdded = false,
                error = null
            )
            PersonListIntent.AddPerson -> addPerson()
            PersonListIntent.AddRelationship -> addRelationship()
            PersonListIntent.LoadPersons -> loadPersons()
        }
    }

    private fun loadPersons() {
        viewModelScope.launch {
            firestoreService.getAllPersonsFlow().collectLatest { persons ->
                _state.value = _state.value.copy(allPersons = persons)
            }
        }
    }

    private fun addPerson() {
        viewModelScope.launch {
            try {
                val person = Person(
                    id = Uuid.random().toString(),
                    firstName = _state.value.personFirstName,
                    lastName = _state.value.personLastName,
                    birthDate = _state.value.personDob,
                    gender = _state.value.gender,
                )
                firestoreService.addPerson(person)
                _state.value = _state.value.copy(personAdded = true)
                _state.value = _state.value.copy(personFirstName = "")
                _state.value = _state.value.copy(personLastName = "")
                _state.value = _state.value.copy(personMiddleName = "")
                _state.value = _state.value.copy(personDob = "")
                _state.value = _state.value.copy(gender = "")
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    private fun addRelationship() {
        viewModelScope.launch {
            try {
                val person1 = _state.value.allPersons.find { it.firstName + " " + it.lastName == _state.value.relationshipPerson1Name }
                val person2 = _state.value.allPersons.find { it.firstName + " " + it.lastName == _state.value.relationshipPerson2Name }

                if (person1 != null && person2 != null) {
                    val relationship = Relationship(
                        id = Uuid.random().toString(),
                        person1Id = person1.id,
                        person2Id = person2.id,
                        relationshipType = _state.value.selectedRelationshipType,
                        startDate = _state.value.relationshipStartDate,
                        notes = _state.value.relationshipNotes.takeIf { it.isNotEmpty() },
                    )
                    firestoreService.addRelationship(relationship)
                    _state.value = _state.value.copy(relationshipAdded = true)
                    _state.value = _state.value.copy(relationshipPerson1Name = "")
                    _state.value = _state.value.copy(relationshipPerson2Name = "")
                    _state.value = _state.value.copy(relationshipStartDate = "")
                    _state.value = _state.value.copy(relationshipEndDate = "")
                    _state.value = _state.value.copy(relationshipNotes = "")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}