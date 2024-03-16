package com.ktt.locamem.data

import com.ktt.locamem.model.User
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class UserRepositoryImpl(private val realm: Realm) : UserRepository {

    override fun getUsers(): Flow<ResultsChange<User>> {
        return realm.query<User>().find().asFlow()
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
            val user = query<User>("_id == $0", userId).find().firstOrNull()
            user?.let { delete(it) }
        }
    }

}