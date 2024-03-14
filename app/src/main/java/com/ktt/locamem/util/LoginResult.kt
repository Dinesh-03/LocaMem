package com.ktt.locamem.util

import com.ktt.locamem.model.User

sealed class LoginResult {

    data object Success : LoginResult()

    data class Failure(val failureType: FailureType) : LoginResult()
}