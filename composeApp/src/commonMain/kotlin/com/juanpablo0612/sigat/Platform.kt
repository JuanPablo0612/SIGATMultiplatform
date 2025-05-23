package com.juanpablo0612.sigat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform