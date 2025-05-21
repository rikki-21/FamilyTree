package org.kollarappa.familytree

// composeApp/src/commonMain/kotlin/org/kollarappa/familytree/di/FirestoreServiceProvider.kt


import org.kollarappa.familytree.FirestoreService

expect object FirestoreServiceProvider {
    fun provideFirestoreService(): FirestoreService
}