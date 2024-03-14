package com.ktt.locamem.data

import com.ktt.locamem.model.User
import com.ktt.locamem.util.LoginResult
import org.mongodb.kbson.ObjectId

interface UserRepository {
    fun getUsers(): List<User>
    suspend fun getUser(userName: String): User?
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: ObjectId)
}