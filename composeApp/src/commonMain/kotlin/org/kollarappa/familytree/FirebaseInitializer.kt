package org.kollarappa.familytree


import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.auth.auth

object FirebaseInitializer {
    fun initialize() {
        // No platform specific code needed here.
        // firebase will initialize in each platform.
    }
    val firestore = Firebase.firestore
    val auth = Firebase.auth
}