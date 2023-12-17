package com.example.wanderwise.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.wanderwise.R
import com.example.wanderwise.databinding.ActivityRegisterBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.login.LoginScreenActivity
import com.example.wanderwise.data.Result
import com.example.wanderwise.ui.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val intentRegister = Intent(this, LoginScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentRegister)
        }

        binding.loginButtonText.setOnClickListener {
            val intentLogin = Intent(this, LoginScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentLogin)
        }

        binding.registerButton.setOnClickListener {
            val name = binding.edUsername.text.toString()
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (name.isEmpty()) {
                binding.edUsername.error = getString(R.string.cannot_empty)
            } else if (email.isEmpty()) {
                binding.edLoginEmail.error = getString(R.string.cannot_empty)
            } else if (password.isEmpty()) {
                binding.edLoginPassword.error = getString(R.string.cannot_empty)
            } else {
                registViewModel.registerUser(name, email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                isLoading(true)
                            }

                            is Result.Success -> {
                                showToast(result.data)
                                isLoading(false)

                                val intentMain = Intent(this, LoginScreenActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                                startActivity(intentMain)
                            }

                            is Result.Error -> {
                                showToast(result.error)
                                isLoading(false)
                            }
                        }
                    }
                }
            }
        }

        supportActionBar?.hide()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

}