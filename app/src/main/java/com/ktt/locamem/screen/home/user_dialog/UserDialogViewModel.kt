package com.ktt.locamem.screen.home.user_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDialogViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun getUsers(userName: String) {
        viewModelScope.launch {
            userRepository.getUsers().collect {
                val userList = it.list.filter { user ->
                    user.userName != userName
                }
                _users.value = userList
            }
        }
    }
}