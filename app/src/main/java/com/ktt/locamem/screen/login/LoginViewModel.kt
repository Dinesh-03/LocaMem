package com.ktt.locamem.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository):
    ViewModel() {
    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            if (userName.isEmpty()) _result.value = Result.Failure(FailureType.EMPTY_USER_NAME)
            else if (password.isEmpty()) _result.value = Result.Failure(FailureType.EMPTY_PASSWORD)
            else {
                val user = userRepository.getUser(userName)
                _result.value = if (user != null) {
                    if (user.password == password) {
                        Result.Success(user)
                    } else {
                        Result.Failure(FailureType.WRONG_PASSWORD)
                    }
                } else {
                    Result.Failure(FailureType.INVALID_USERNAME)
                }
            }
        }
    }

}