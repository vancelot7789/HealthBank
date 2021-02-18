package com.example.healthbank.Utilities

import java.security.MessageDigest

object Sha256 {
    fun hash(data:String): String {
        val bytes = data.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}