package com.ktt.locamem.screen.login.forgot_password

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
class ForgotPasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _status = MutableLiveData<Result>()
    val status: LiveData<Result> = _status

    fun changePassword(
        userName: String,
        newPassword: String,
        retypePassword: String,
        captcha: String,
        captchaGenerated: String
    ) {
        viewModelScope.launch {
            if (userName.isEmpty()) _status.value =  Result.Failure(FailureType.EMPTY_USER_NAME)
            else if (newPassword.isEmpty()) _status.value =  Result.Failure(FailureType.EMPTY_PASSWORD)
            else if (retypePassword.isEmpty()) _status.value =  Result.Failure(FailureType.RETYPE_EMPTY_PASSWORD)
            else if (captcha.isEmpty()) _status.value = Result.Failure(FailureType.EMPTY_CAPTCHA)
            else if (retypePassword != newPassword) _status.value =  Result.Failure(FailureType.RETYPE_PASSWORD_NOT_MATCHED)
            else if (!Util.isValidPassword(newPassword)) _status.value =  Result.Failure(FailureType.INVALID_PASSWORD)
            else if (captcha != captchaGenerated) _status.value = Result.Failure(FailureType.CAPTCHA_NOT_MATCHED)
            else {
                var user = userRepository.getUser(userName)
                if (user == null) _status.value = Result.Failure(FailureType.INVALID_USERNAME)
                else {
                    user = User(userName, newPassword)
                    userRepository.updateUser(user)
                    _status.value = Result.Success(user)
                }
            }
        }
    }

}