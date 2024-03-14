package com.ktt.locamem.data

import android.util.Log
import com.ktt.locamem.model.User
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId

class UserRepositoryImpl(private val realm: Realm) : UserRepository {

    override fun getUsers(): List<User> {
        return realm.query<User>().find()
    }

    override suspend fun getUser(userName: String): User? {
        return realm.query<User>("_userName == $0", userName).find().firstOrNull()
    }

    override suspend fun insertUser(user: User) {
        realm.write {
            copyToRealm(user)
        }
    }

    override suspend fun updateUser(user: User) {
        realm.write {
//            findLatest(user)?.let { updatedUser ->
//                updatedUser.userName = user.userName
//                updatedUser.password = user.password
//            }
        }
    }

    override suspend fun deleteUser(userId: ObjectId) {
        realm.write {
            val user: User = query<User>("_id", userId).find().first()
            delete(user)
        }
    }

}