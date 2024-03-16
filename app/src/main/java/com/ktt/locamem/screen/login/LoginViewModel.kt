package com.ktt.locamem.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository):
    ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            if (userName.isEmpty()) _loginResult.value = LoginResult.Failure(FailureType.EMPTY_USER_NAME)
            else if (password.isEmpty()) _loginResult.value = LoginResult.Failure(FailureType.EMPTY_PASSWORD)
            else {
                val user = userRepository.getUser(userName)
                _loginResult.value = if (user != null) {
                    if (user.password == password) {
                        LoginResult.Success(user)
                    } else {
                        LoginResult.Failure(FailureType.WRONG_PASSWORD)
                    }
                } else {
                    LoginResult.Failure(FailureType.INVALID_USERNAME)
                }
            }
        }
    }

}