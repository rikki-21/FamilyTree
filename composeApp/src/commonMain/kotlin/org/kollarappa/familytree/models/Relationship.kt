package org.kollarappa.familytree.models


import kotlinx.serialization.Serializable

@Serializable
data class Relationship(
    val id: String,
    val person1Id: String,
    val person2Id: String,
    val relationshipType: RelationshipType,
    val startDate: String,
    val notes: String?
)

enum class RelationshipType {
    HUSBAND,
    WIFE,
    BROTHER,
    SISTER,
    FATHER,
    MOTHER,
    SON,
    DAUGHTER,
    GRANDFATHER,
    GRANDMOTHER,
    GRANDSON,
    GRANDDAUGHTER,
    UNCLE,
    AUNT,
    NEPHEW,
    NIECE,
    COUSIN,
    FATHER_IN_LAW,
    MOTHER_IN_LAW,
    SON_IN_LAW,
    DAUGHTER_IN_LAW,
    BROTHER_IN_LAW,
    SISTER_IN_LAW,
 }