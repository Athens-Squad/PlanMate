package net.thechance.data.authentication.utils

import java.security.MessageDigest

class PasswordHashing {
    fun hash(password: String): String {
        val md = MessageDigest.getInstance("MD5")
        val hash = md.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }     }
}