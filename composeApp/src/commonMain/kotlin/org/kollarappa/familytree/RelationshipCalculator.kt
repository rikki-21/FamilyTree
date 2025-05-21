package org.kollarappa.familytree

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.kollarappa.familytree.models.Relationship
import org.kollarappa.familytree.models.RelationshipType

class RelationshipCalculator(private val firestoreService: FirestoreService) {

    suspend fun areSiblings(person1Id: String, person2Id: String): Boolean {
        val person1 = firestoreService.getPersonFlow(person1Id).firstOrNull() ?: return false
        val person2 = firestoreService.getPersonFlow(person2Id).firstOrNull() ?: return false

        if (person1.id == person2.id) return false

        val relationships: List<Relationship> = firestoreService.getAllRelationships().firstOrNull() ?: emptyList()

        val person1Parents = relationships.filter { relationship ->
            (relationship.relationshipType == RelationshipType.MOTHER || (relationship.relationshipType == RelationshipType.FATHER) && relationship.person2Id == person1.id)
        }.map { it.person1Id }

        val person2Parents = relationships.filter { relationship ->
            (relationship.relationshipType == RelationshipType.MOTHER || (relationship.relationshipType == RelationshipType.FATHER) && relationship.person2Id == person2.id)
        }.map { it.person1Id }

        return person1Parents.intersect(person2Parents.toSet()).isNotEmpty()
    }
}