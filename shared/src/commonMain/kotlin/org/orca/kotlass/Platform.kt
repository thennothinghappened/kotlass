package org.orca.kotlass

expect val platform: String

class Greeting {
    fun greeting() = "Hello, $platform!"
}