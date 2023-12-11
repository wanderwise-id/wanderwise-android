package com.example.wanderwise.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.wanderwise.R
import com.example.wanderwise.ui.login.LoginScreenActivity

class FirstScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        val changeTime = object : CountDownTimer(1000L, 500) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val intentMain = Intent(this@FirstScreenActivity, LoginScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intentMain)
                finish()
            }
        }
        changeTime.start()

        supportActionBar?.hide()
    }
}