package net.thechance.data.utils

fun loadEnvironmentVariable(key: String): String {
    return System.getenv(key)
}