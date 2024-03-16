package com.ktt.locamem.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class LocationData(): RealmObject {
    @PrimaryKey
    private var _id: ObjectId = ObjectId()
    private var _userName: String = ""
    private var _latitude: String = ""
    private var _longitude: String = ""
    private var _timeStamp: Long = 0L
    val id: ObjectId
        get() = _id
    val userName: String
        get() = _userName
    val latitude: String
        get() = _latitude
    val longitude: String
        get() = _longitude
    val timeStamp: Long
        get() = _timeStamp

    constructor(userName: String, latitude: String = "", longitude: String = "", timeStamp: Long) : this() {
        _userName = userName
        _latitude = latitude
        _longitude = longitude
        _timeStamp = timeStamp
    }
}