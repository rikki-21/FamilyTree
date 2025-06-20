package org.kollarappa.familytree.ui.personlist

import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.RelationshipType


sealed class PersonListIntent {
    data class PersonFirstNameChanged(val name: String) : PersonListIntent()
    data class PersonLastNameChanged(val name: String) : PersonListIntent()
    data class PersonMiddleNameChanged(val name: String) : PersonListIntent()
    data class PersonAgeChanged(val age: String) : PersonListIntent()
    data class PersonGenderChanged(val gender: String) : PersonListIntent()
//    data class RelationshipPerson1NameChanged(val name: String) : PersonListIntent()
//    data class RelationshipPerson2NameChanged(val name: String) : PersonListIntent()
    data class RelationshipTypeChanged(val type: RelationshipType) : PersonListIntent()
    data class RelationshipStartDateChanged(val date: String) : PersonListIntent()
    data class RelationshipEndDateChanged(val date: String) : PersonListIntent()
    data class RelationshipNotesChanged(val notes: String) : PersonListIntent()
    object AddPerson : PersonListIntent()
    object AddRelationship : PersonListIntent()
    object LoadPersons : PersonListIntent()
    object ClearMessages : PersonListIntent()
    data class SearchPerson1(val query: String) : PersonListIntent()
    data class SelectPerson1(val person: Person) : PersonListIntent()
    object ClearPerson1Selection : PersonListIntent() // To clear search/selection
    data class SearchPerson2(val query: String) : PersonListIntent()
    data class SelectPerson2(val person: Person) : PersonListIntent()
    object ClearPerson2Selection : PersonListIntent()
}