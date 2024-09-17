package com.munywele.sms.utils

import java.security.MessageDigest

object StringUtils {
    fun generateMessageHash(message: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(message.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}