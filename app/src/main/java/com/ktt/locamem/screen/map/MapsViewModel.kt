package com.ktt.locamem.screen.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.LocationRepository
import com.ktt.locamem.model.LocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val locationRepository: LocationRepository): ViewModel() {
    private val _locations = MutableLiveData<List<LocationData>>()
    val locations: LiveData<List<LocationData>> = _locations

    fun getLocations(userName: String) {
        viewModelScope.launch {
            locationRepository.getLocations(userName).collect {
                _locations.value = it.list
            }
        }
    }
}