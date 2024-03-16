package com.ktt.locamem.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.LocationRepository
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.model.LocationData
import com.ktt.locamem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locations = MutableLiveData<List<LocationData>>()
    val locations: LiveData<List<LocationData>> = _locations
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun getLocations(userName: String) {
        viewModelScope.launch {
            locationRepository.getLocations(userName).collect {
                _locations.value = it.list
            }
        }
    }

    fun getUsers() {
        viewModelScope.launch {
            userRepository.getUsers().collect {
                _users.value = it.list
            }
        }
    }

    fun addLocationData(userName: String, latitude: String, longitude: String, timeStamp: Long) {
        viewModelScope.launch {
            val locationData = LocationData(userName, latitude, longitude, timeStamp)
            locationRepository.insertLocationData(locationData)
        }
    }
}