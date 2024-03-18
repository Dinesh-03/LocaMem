package com.ktt.locamem.screen.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.model.User
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.Result
import com.ktt.locamem.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _signupResult = MutableLiveData<Result>()
    val signupResult: LiveData<Result> = _signupResult

    fun signup(userName: String, password: String, retypePassword: String) {
        viewModelScope.launch {
            if (userName.isEmpty()) _signupResult.value =  Result.Failure(FailureType.EMPTY_USER_NAME)
            else if (password.isEmpty()) _signupResult.value =  Result.Failure(FailureType.EMPTY_PASSWORD)
            else if (retypePassword.isEmpty()) _signupResult.value =  Result.Failure(FailureType.RETYPE_EMPTY_PASSWORD)
            else if (retypePassword != password) _signupResult.value =  Result.Failure(FailureType.RETYPE_PASSWORD_NOT_MATCHED)
            else if (!Util.isValidPassword(password)) _signupResult.value =  Result.Failure(FailureType.INVALID_PASSWORD)
            else {
                var user = userRepository.getUser(userName)
//                user?.let { _signupResult.value = Result.Failure(FailureType.USER_EXIST) }
//                    ?: {
//                        val newUser = User(userName, password)
//                        this.launch {
//                            userRepository.insertUser(newUser)
//                        }
//                        _signupResult.value = Result.Success
//                    }
                if (user != null) _signupResult.value = Result.Failure(FailureType.USER_EXIST)
                else {
                    user = User(userName, password)
                    userRepository.insertUser(user)
                    _signupResult.value = Result.Success(user)
                }
            }
        }
    }

}