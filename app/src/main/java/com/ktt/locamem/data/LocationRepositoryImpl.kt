package com.ktt.locamem.data

import com.ktt.locamem.model.LocationData
import com.ktt.locamem.model.User
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class LocationRepositoryImpl(private val realm:Realm): LocationRepository {
    override fun getLocations(userName: String): Flow<ResultsChange<LocationData>> {
        return realm.query<LocationData>("_userName ='$userName'").find().asFlow()
    }

    override suspend fun getLocationData(locationId: ObjectId): LocationData? {
        return realm.query<LocationData>("_id == $0", locationId).find().firstOrNull()
    }

    override suspend fun insertLocationData(locationData: LocationData) {
        realm.write {
            copyToRealm(locationData)
        }
    }

    override suspend fun updateLocationData(locationData: LocationData) {
        realm.write {
//            copyToRealm(locationData)
        }
    }

    override suspend fun deleteLocationData(locationId: ObjectId) {
        realm.write {
            val locationData = query<LocationData>("_id == $0", locationId).find().firstOrNull()
            locationData?.let { delete(it) }
        }
    }

}