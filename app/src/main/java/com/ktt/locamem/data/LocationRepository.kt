package com.ktt.locamem.data

import com.ktt.locamem.model.LocationData
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface LocationRepository {
    fun getLocations(userName: String): Flow<ResultsChange<LocationData>>
    suspend fun getLocationData(locationId: ObjectId): LocationData?
    suspend fun insertLocationData(locationData: LocationData)
    suspend fun updateLocationData(locationData: LocationData)
    suspend fun deleteLocationData(locationId: ObjectId)
}