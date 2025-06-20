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

enum class RelationshipType(val displayName: String) {
    HUSBAND("Husband"),
    WIFE("Wife"),
    BROTHER("Brother"),
    SISTER("Sister"),
    FATHER("Father"),
    MOTHER("Mother"),
    SON("Son"),
    DAUGHTER("Daughter"),
    GRANDFATHER("Grandfather"),
    GRANDMOTHER("Grandmother"),
    GRANDSON("Grandson"),
    GRANDDAUGHTER("Granddaughter"),
    UNCLE("Uncle"),
    AUNT("Aunt"),
    NEPHEW("Nephew"),
    NIECE("Niece"),
    COUSIN("Cousin"),
    FATHER_IN_LAW("Father-in-Law"),
    MOTHER_IN_LAW("Mother-in-Law"),
    SON_IN_LAW("Son-in-Law"),
    DAUGHTER_IN_LAW("Daughter-in-Law"),
    BROTHER_IN_LAW("Brother-in-Law"),
    SISTER_IN_LAW("Sister-in-Law"),
}