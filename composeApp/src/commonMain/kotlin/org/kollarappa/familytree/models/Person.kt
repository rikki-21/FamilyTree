package org.kollarappa.familytree.models


import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String? = null,
    val gender: String? = null,
    // ... other fields
)