package com.example.wanderwise.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wanderwise.ui.MainActivity
import com.example.wanderwise.ui.register.RegisterActivity
import com.example.wanderwise.databinding.ActivityLoginScreenBinding

class LoginScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val intentMain = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentMain)
        }

        binding.registerButtonText.setOnClickListener {
            val intentRegister = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentRegister)
        }

        supportActionBar?.hide()
    }
}