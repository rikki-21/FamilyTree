package org.kollarappa.familytree.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private var searchJob1: Job? = null
    private var searchJob2: Job? = null

    init {
        // Initial data loading can be kept if needed for other parts,
        // or removed if allPersons is solely for the old dropdowns.
        // handleIntent(PersonListIntent.LoadPersons)
    }

    fun handleIntent(intent: PersonListIntent) {
        when (intent) {
            is PersonListIntent.PersonFirstNameChanged -> _state.value = _state.value.copy(personFirstName = intent.name)
            is PersonListIntent.PersonLastNameChanged -> _state.value = _state.value.copy(personLastName = intent.name)
            is PersonListIntent.PersonMiddleNameChanged -> _state.value = _state.value.copy(personMiddleName = intent.name)
            is PersonListIntent.PersonAgeChanged -> _state.value = _state.value.copy(personDob = intent.age)
            is PersonListIntent.PersonGenderChanged -> _state.value = _state.value.copy(gender = intent.gender)
            // Removed RelationshipPerson1NameChanged and RelationshipPerson2NameChanged as they are replaced by search
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
            PersonListIntent.LoadPersons -> loadPersons() // Kept if still needed

            is PersonListIntent.SearchPerson1 -> searchPerson1(intent.query)
            is PersonListIntent.SelectPerson1 -> {
                _state.value = _state.value.copy(
                    selectedPerson1 = intent.person,
                    person1SearchQuery = "${intent.person.firstName} ${intent.person.lastName}", // Display selected name
                    person1SearchResults = emptyList()
                )
            }

            is PersonListIntent.SearchPerson2 -> searchPerson2(intent.query)
            is PersonListIntent.SelectPerson2 -> {
                _state.value = _state.value.copy(
                    selectedPerson2 = intent.person,
                    person2SearchQuery = "${intent.person.firstName} ${intent.person.lastName}", // Display selected name
                    person2SearchResults = emptyList()
                )
            }
            is PersonListIntent.ClearPerson1Selection -> clearPerson1Selection()
            is PersonListIntent.ClearPerson2Selection -> clearPerson2Selection()
        }
    }

    private fun clearPerson1Selection() {
        searchJob1?.cancel() // Assuming searchJob1 is the Job for person 1 search
        _state.value = _state.value.copy(
            selectedPerson1 = null,
            person1SearchQuery = "",
            person1SearchResults = emptyList(),
            isSearchingPerson1 = false
        )
    }

    // Similar function for clearPerson2Selection()
    private fun clearPerson2Selection() {
        searchJob2?.cancel()
        _state.value = _state.value.copy(
            selectedPerson2 = null,
            person2SearchQuery = "",
            person2SearchResults = emptyList(),
            isSearchingPerson2 = false
        )
    }

    private fun searchPerson1(query: String) {
        _state.value = _state.value.copy(person1SearchQuery = query, selectedPerson1 = null) // Clear selection on new search
        searchJob1?.cancel() // Cancel previous search job
        if (query.isBlank()) {
            _state.value = _state.value.copy(person1SearchResults = emptyList(), isSearchingPerson1 = false)
            return
        }
        _state.value = _state.value.copy(isSearchingPerson1 = true)
        searchJob1 = viewModelScope.launch {
            delay(300) // Debounce
            try {
                val results = firestoreService.searchPersonsByName(query)
                _state.value = _state.value.copy(person1SearchResults = results, isSearchingPerson1 = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isSearchingPerson1 = false, person1SearchResults = emptyList())
            }
        }
    }

    private fun searchPerson2(query: String) {
        _state.value = _state.value.copy(person2SearchQuery = query, selectedPerson2 = null) // Clear selection on new search
        searchJob2?.cancel() // Cancel previous search job
        if (query.isBlank()) {
            _state.value = _state.value.copy(person2SearchResults = emptyList(), isSearchingPerson2 = false)
            return
        }
        _state.value = _state.value.copy(isSearchingPerson2 = true)
        searchJob2 = viewModelScope.launch {
            delay(300) // Debounce
            try {
                val results = firestoreService.searchPersonsByName(query)
                _state.value = _state.value.copy(person2SearchResults = results, isSearchingPerson2 = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isSearchingPerson2 = false, person2SearchResults = emptyList())
            }
        }
    }


    private fun loadPersons() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            firestoreService.getAllPersonsFlow().collectLatest { persons ->
                _state.value = _state.value.copy(allPersons = persons, isLoading = false)
            }
        }
    }

    private fun addPerson() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val person = Person(
                    id = Uuid.random().toString(),
                    firstName = _state.value.personFirstName,
                    lastName = _state.value.personLastName,
                    birthDate = _state.value.personDob, // Ensure this is correctly formatted or validated
                    gender = _state.value.gender,
                )
                firestoreService.addPerson(person)
                _state.value = _state.value.copy(
                    personAdded = true,
                    personFirstName = "",
                    personLastName = "",
                    personMiddleName = "",
                    personDob = "",
                    gender = "",
                    error = null,
                    isLoading = false
                )
                // Optionally reload all persons if your UI depends on it immediately
                // loadPersons()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false, personAdded = false)
            }
        }
    }

    private fun addRelationship() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val person1Id = _state.value.selectedPerson1?.id
                val person2Id = _state.value.selectedPerson2?.id

                if (person1Id != null && person2Id != null) {
                    if (person1Id == person2Id) {
                        _state.value = _state.value.copy(error = "Cannot create a relationship with the same person.", isLoading = false)
                        return@launch
                    }
                    val relationship = Relationship(
                        id = Uuid.random().toString(),
                        person1Id = person1Id,
                        person2Id = person2Id,
                        relationshipType = _state.value.selectedRelationshipType,
                        startDate = _state.value.relationshipStartDate, // Ensure validation
                        notes = _state.value.relationshipNotes.takeIf { it.isNotEmpty() },
                    )
                    firestoreService.addRelationship(relationship)
                    _state.value = _state.value.copy(
                        relationshipAdded = true,
                        // Clear relationship form fields
                        selectedPerson1 = null,
                        person1SearchQuery = "",
                        person1SearchResults = emptyList(),
                        selectedPerson2 = null,
                        person2SearchQuery = "",
                        person2SearchResults = emptyList(),
                        relationshipStartDate = "",
                        relationshipEndDate = "",
                        relationshipNotes = "",
                        error = null,
                        isLoading = false
                    )
                } else {
                    _state.value = _state.value.copy(error = "Please select both persons for the relationship.", isLoading = false)
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false, relationshipAdded = false)
            }
        }
    }
}