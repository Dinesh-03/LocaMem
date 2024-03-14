package com.ktt.locamem.screen.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
import com.google.android.material.textfield.TextInputLayout.EndIconMode
import com.ktt.locamem.databinding.ActivityLoginScreenBinding
import com.ktt.locamem.screen.home.HomeScreenActivity
import com.ktt.locamem.screen.signup.SignUpScreenActivity
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.FailureType
import com.ktt.locamem.util.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.loginResult.observe(this) {
            when (it) {
                LoginResult.Success -> {
                    startActivity(Intent(this, HomeScreenActivity::class.java))
                    finish()
                }

                is LoginResult.Failure -> {
                    when (it.failureType) {
                        FailureType.INVALID_USERNAME -> {
                            binding.userNameIl.error = Constants.INVALID_USERNAME
                            binding.passwordIl.isErrorEnabled = false
                        }
                        FailureType.EMPTY_USER_NAME -> {
                            binding.userNameIl.error = Constants.EMPTY_USER_NAME
                            binding.passwordIl.isErrorEnabled = false
                        }
                        FailureType.EMPTY_PASSWORD -> {
                            binding.passwordIl.error = Constants.EMPTY_PASSWORD
                            binding.userNameIl.isErrorEnabled = false
                        }
                        FailureType.WRONG_PASSWORD -> {
                            binding.passwordIl.error = Constants.WRONG_PASSWORD
                            binding.userNameIl.isErrorEnabled = false
                        }

                        FailureType.RETYPE_EMPTY_PASSWORD -> {}
                        FailureType.RETYPE_PASSWORD_NOT_MATCHED -> {}
                        FailureType.INVALID_PASSWORD -> {}
                        FailureType.USER_EXIST -> {}
                    }
                }
            }
        }

        binding.password.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordIl.isErrorEnabled = false
            }
        }

        binding.login.setOnClickListener {
            val userName:String = binding.userName.text.toString().trim()
            val password:String = binding.password.text.toString().trim()
            loginViewModel.login(userName, password)
        }

        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUpScreenActivity::class.java))
        }

    }
}