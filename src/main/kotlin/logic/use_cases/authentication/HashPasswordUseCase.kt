package net.thechance.logic.use_cases.authentication

import java.security.MessageDigest

class HashPasswordUseCase {
    fun execute(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }     }
}