package org.kollarappa.familytree.ui.personlist


import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.RelationshipType


data class PersonListState(
    val personFirstName: String = "",
    val personLastName: String = "",
    val personMiddleName: String = "",
    val personDob: String = "",
    val gender: String = "",
    val relationshipPerson1Name: String = "",
    val relationshipPerson2Name: String = "",
    val selectedRelationshipType: RelationshipType = RelationshipType.HUSBAND,
    val relationshipStartDate: String = "",
    val relationshipEndDate: String = "",
    val relationshipNotes: String = "",
    val allPersons: List<Person> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val personAdded: Boolean = false,
    val relationshipAdded: Boolean = false,
)