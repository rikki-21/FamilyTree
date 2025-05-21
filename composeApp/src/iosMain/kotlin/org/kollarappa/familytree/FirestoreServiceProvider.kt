package org.kollarappa.familytree

// composeApp/src/iosMain/kotlin/org/kollarappa/familytree/di/FirestoreServiceProvider.kt


import org.kollarappa.familytree.FirestoreService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

actual object FirestoreServiceProvider {
    actual fun provideFirestoreService(): FirestoreService {
        return FirestoreService(Firebase.firestore)
    }
}