package com.example.wanderwise.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.wanderwise.databinding.ActivityRegisterBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.login.LoginScreenActivity
import com.example.wanderwise.data.Result

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

            Log.d("IsiNameEmailPassword", "$name, $email, $password")
            registViewModel.registerUser(name, email, password).observe(this@RegisterActivity) {
                Toast.makeText(this, "Yeay its work", Toast.LENGTH_SHORT).show()
            }
        }

        supportActionBar?.hide()
    }
}