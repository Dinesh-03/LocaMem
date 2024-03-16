package com.ktt.locamem.util

import com.ktt.locamem.model.User

sealed class LoginResult {

    data class Success(val user: User) : LoginResult()

    data class Failure(val failureType: FailureType) : LoginResult()
}