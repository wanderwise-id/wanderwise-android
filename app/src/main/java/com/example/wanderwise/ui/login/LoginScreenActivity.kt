package com.example.wanderwise.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.wanderwise.ui.MainActivity
import com.example.wanderwise.ui.register.RegisterActivity
import com.example.wanderwise.databinding.ActivityLoginScreenBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.data.Result

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.getSessionUser().observe(this) { user ->
            if (user.isLogin) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.loginButton.setOnClickListener {

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            loginViewModel.loginUser(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }

                        is Result.Success -> {
                            showToast(result.data)

                            val intentMain = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            startActivity(intentMain)
                        }

                        is Result.Error -> {
                            showToast(result.error)
                        }
                    }
                }
            }
        }

        binding.registerButtonText.setOnClickListener {
            val intentRegister = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentRegister)
        }

        supportActionBar?.hide()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}