package com.ktt.locamem.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User() : RealmObject {
    @PrimaryKey
    private var _id: ObjectId = ObjectId()
    private var _userName: String = ""
    private var _password: String = ""
    val userName: String
        get() = _userName
    val password: String
        get() = _password
    val id: ObjectId
        get() = _id
    constructor(userName: String, password: String) : this() {
        _userName = userName
        _password = password
    }



}
