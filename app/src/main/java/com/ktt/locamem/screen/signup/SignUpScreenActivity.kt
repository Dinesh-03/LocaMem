package com.ktt.locamem.screen.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ktt.locamem.databinding.ActivitySignupScreenBinding
import com.ktt.locamem.screen.home.HomeScreenActivity
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupScreenBinding
    private lateinit var signUpViewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        binding.back.setOnClickListener {
            finish()
        }

        signUpViewModel.signupResult.observe(this) {
            when (it) {
                is Result.Success -> {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
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

                        FailureType.INVALID_USERNAME -> {}
                        FailureType.WRONG_PASSWORD -> {}
                        FailureType.CAPTCHA_NOT_MATCHED -> {}
                        FailureType.EMPTY_CAPTCHA -> {}
                    }
                }
            }
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

        binding.signup.setOnClickListener {
            val userName:String = binding.userName.text.toString().trim()
            val password:String = binding.password.text.toString().trim()
            val retypePassword:String = binding.retypePassword.text.toString().trim()
            signUpViewModel.signup(userName, password, retypePassword)
        }

        binding.reset.setOnClickListener {
            binding.userName.setText(Constants.EMPTY)
            binding.password.setText(Constants.EMPTY)
            binding.retypePassword.setText(Constants.EMPTY)
            binding.userNameIl.isErrorEnabled = false
            binding.passwordIl.isErrorEnabled = false
            binding.retypePasswordIl.isErrorEnabled = false
        }

    }
}