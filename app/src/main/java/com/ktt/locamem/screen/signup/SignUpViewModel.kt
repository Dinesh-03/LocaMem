package com.ktt.locamem.screen.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.model.User
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _signupResult = MutableLiveData<LoginResult>()
    val signupResult: LiveData<LoginResult> = _signupResult

    fun signup(userName: String, password: String, retypePassword: String) {
        viewModelScope.launch {
            if (userName.isEmpty()) _signupResult.value =  LoginResult.Failure(FailureType.EMPTY_USER_NAME)
            else if (password.isEmpty()) _signupResult.value =  LoginResult.Failure(FailureType.EMPTY_PASSWORD)
            else if (retypePassword.isEmpty()) _signupResult.value =  LoginResult.Failure(FailureType.RETYPE_EMPTY_PASSWORD)
            else if (retypePassword != password) _signupResult.value =  LoginResult.Failure(FailureType.RETYPE_PASSWORD_NOT_MATCHED)
            else if (!isValidPassword(password)) _signupResult.value =  LoginResult.Failure(FailureType.INVALID_PASSWORD)
            else {
                var user = userRepository.getUser(userName)
                if (user != null) _signupResult.value = LoginResult.Failure(FailureType.USER_EXIST)
                else {
                    user = User(userName, password)
                    userRepository.insertUser(user)
                    _signupResult.value = LoginResult.Success
                }
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$"
        return password.matches(regex.toRegex())
    }
}