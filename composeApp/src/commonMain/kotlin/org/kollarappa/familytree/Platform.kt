package org.kollarappa.familytree

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform