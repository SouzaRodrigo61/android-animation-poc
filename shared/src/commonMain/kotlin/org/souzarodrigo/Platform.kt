package org.souzarodrigo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform