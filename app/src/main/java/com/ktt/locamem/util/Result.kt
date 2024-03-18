package com.ktt.locamem.util

import com.ktt.locamem.model.User

sealed class Result {

    data class Success(val user: User) : Result()

    data class Failure(val failureType: FailureType) : Result()
}