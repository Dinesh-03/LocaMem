package com.ktt.locamem.util

object Util {
    fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$"
        return password.matches(regex.toRegex())
    }

    fun generateRandomString(size: Int = 6): String {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '!' + '@' + '#' + '$' + '%' + '&' + '*'
        val randomString = StringBuilder(size)
        for (i in 0 until size) {
            randomString.append(charPool.random())
        }
        return randomString.toString()
    }
}