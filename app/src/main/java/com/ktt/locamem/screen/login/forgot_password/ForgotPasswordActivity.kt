package com.ktt.locamem.screen.login.forgot_password

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ktt.locamem.databinding.ActivityForgotPasswordBinding
import com.ktt.locamem.screen.home.HomeScreenActivity
import com.ktt.locamem.screen.login.LoginScreenActivity
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.Result
import com.ktt.locamem.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private lateinit var captchaGenerated: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forgotPasswordViewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        captchaGenerated = Util.generateRandomString()
        binding.captcha.text = captchaGenerated
        binding.update.setOnClickListener {
            val userName = binding.userName.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val retypePassword = binding.retypePassword.text.toString().trim()
            val captcha = binding.confirmCaptcha.text.toString().trim()
            forgotPasswordViewModel.changePassword(userName, password, retypePassword, captcha, captchaGenerated)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                binding.passwordIl.isErrorEnabled = false
            }
        }

        binding.retypePassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                binding.retypePasswordIl.isErrorEnabled = false
            }
        }

        forgotPasswordViewModel.status.observe(this) {
            when (it) {
                is Result.Success -> {
                    startActivity(Intent(this, LoginScreenActivity::class.java))
                    finish()
                }

                is Result.Failure -> {
                    when (it.failureType) {
                        FailureType.EMPTY_USER_NAME -> {
                            binding.userNameIl.error = Constants.EMPTY_USER_NAME
                            binding.passwordIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                        }
                        FailureType.USER_EXIST -> {
                            binding.userNameIl.error = Constants.USER_EXIST
                            binding.passwordIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                        }
                        FailureType.EMPTY_PASSWORD -> {
                            binding.passwordIl.error = Constants.EMPTY_PASSWORD
                            binding.userNameIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                        }
                        FailureType.INVALID_PASSWORD -> {
                            binding.passwordIl.error = Constants.INVALID_PASSWORD
                            binding.userNameIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                        }
                        FailureType.RETYPE_EMPTY_PASSWORD -> {
                            binding.retypePasswordIl.error = Constants.RETYPE_EMPTY_PASSWORD
                            binding.userNameIl.isErrorEnabled = false
                            binding.passwordIl.isErrorEnabled = false
                        }
                        FailureType.RETYPE_PASSWORD_NOT_MATCHED -> {
                            binding.retypePasswordIl.error = Constants.RETYPE_PASSWORD_NOT_MATCHED
                            binding.userNameIl.isErrorEnabled = false
                            binding.passwordIl.isErrorEnabled = false
                        }
                        FailureType.CAPTCHA_NOT_MATCHED -> {
                            binding.captchaIl.error = Constants.CAPTCHA_NOT_MATCHED
                            binding.userNameIl.isErrorEnabled = false
                            binding.passwordIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                            captchaGenerated = Util.generateRandomString()
                            binding.captcha.text = captchaGenerated
                        }
                        FailureType.INVALID_USERNAME -> {
                            binding.userNameIl.error = Constants.INVALID_USERNAME
                            binding.passwordIl.isErrorEnabled = false
                        }
                        FailureType.EMPTY_CAPTCHA -> {
                            binding.captchaIl.error = Constants.EMPTY_CAPTCHA
                            binding.userNameIl.isErrorEnabled = false
                            binding.passwordIl.isErrorEnabled = false
                            binding.retypePasswordIl.isErrorEnabled = false
                            captchaGenerated = Util.generateRandomString()
                            binding.captcha.text = captchaGenerated
                        }

                        FailureType.WRONG_PASSWORD -> {}
                    }
                }
            }
        }
    }

}