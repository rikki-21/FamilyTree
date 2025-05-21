package org.kollarappa.familytree

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.tasks.await
import org.kollarappa.familytree.models.Person
import org.kollarappa.familytree.models.Relationship
import org.kollarappa.familytree.models.RelationshipType
import com.google.android.gms.tasks.Task

class FirestoreService(
    private val firestore: FirebaseFirestore = Firebase.firestore,
) {
    private val personCollection = firestore.collection("persons")
    private val relationshipCollection = firestore.collection("relationships")


    suspend fun addPerson(person: Person) {
        personCollection.document(person.id).set(person)
    }

    fun getPersonFlow(personId: String): Flow<Person?> = flow {
        try {
            val documentSnapshot = personCollection.document(personId).get()
            if (documentSnapshot.exists) {
                emit(mapDocumentToPerson(documentSnapshot))
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            emit(null)
        }
    }

    fun getAllPersonsFlow(): Flow<List<Person>> = flow {
        try {
            val snapshot = personCollection.get()
            val persons = snapshot.documents.mapNotNull { document ->
                mapDocumentToPerson(document)
            }
            emit(persons)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    private fun mapDocumentToPerson(document: DocumentSnapshot): Person? {
        return try {
            Person(
                id = document.get<String>("id") as String,
                firstName = document.get<String>("firstName") as String,
                lastName = document.get<String>("lastName") as String,
                birthDate = document.get<String>("birthDate") as String,
                gender = document.get<String>("gender") as String,
            )
        } catch (e: Exception) {
            // Handle mapping errors (e.g., log the error)
            println("Error mapping document to Person: ${e.message}, document: ${document.id}")
            null
        }
    }

    suspend fun addRelationship(relationship: Relationship) {
        relationshipCollection.document(relationship.id).set(relationship)
    }

    fun getAllRelationships(): Flow<List<Relationship>> = flow {
        try {
            val snapshot = relationshipCollection.get()
            val relationships = snapshot.documents.mapNotNull { document ->
                mapDocumentToRelationship(document)
            }
            emit(relationships)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    private fun mapDocumentToRelationship(document: DocumentSnapshot): Relationship? {
        return try {
            Relationship(
                id = document.get<String>("id") as String,
                person1Id = document.get<String>("person1Id") as String,
                person2Id = document.get<String>("person2Id") as String,
                relationshipType = document.get<RelationshipType>("relationshipType"),
                startDate = document.get<String>("startDate") as String,
                notes = document.get<String>("notes") as String
            )
        } catch (e: Exception) {
            // Handle mapping errors (e.g., log the error)
            println("Error mapping document to Relationship: ${e.message}, document: ${document.id}")
            null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getRelationshipsForPerson(personId: String): List<Relationship>? {
        return try {
            val snapshot = firestore.collection("relationships")
                .where("person1Id", "==", personId)
                .where("person2Id", "==", personId)
                .get()
                .await()

            snapshot.documents.mapNotNull { document: DocumentSnapshot ->
                mapDocumentToRelationship(document)
            }
        } catch (e: Exception) {
            // Log the error or handle it as needed
            println("Error fetching relationships for person: ${e.message}")
            null
        }
    }
}