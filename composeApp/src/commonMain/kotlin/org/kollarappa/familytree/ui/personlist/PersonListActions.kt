package org.kollarappa.familytree.ui.personlist

import org.kollarappa.familytree.models.Person


sealed class AddAction {
    data class PersonsLoaded(val persons: List<Person>) : AddAction()
    data class PersonsLoadFailed(val error: String) : AddAction()
    object PersonAddedSuccess: AddAction()
    object PersonAddedFailed: AddAction()
    object RelationshipAddedSuccess: AddAction()
    object RelationshipAddedFailed: AddAction()
}