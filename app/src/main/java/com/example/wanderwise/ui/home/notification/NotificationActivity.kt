package com.example.wanderwise.ui.home.notification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.Notification
import com.example.wanderwise.databinding.ActivityNotificationBinding
import com.example.wanderwise.ui.ViewModelFactory
import com.example.wanderwise.ui.adapter.DestinationAdapter
import com.example.wanderwise.ui.adapter.NotificationAdapter
import com.example.wanderwise.ui.home.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.title = "Notification"

        val cityKey = intent.getStringExtra("cityKey")

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")
        val currentTime = System.currentTimeMillis()
        val oneDayAgo = currentTime - (24 * 60 * 60 * 1000)
        val refNotification = db.getReference("notifications/${cityKey}")//.orderByChild("timestamp").startAt(oneDayAgo.toString().toDouble())
        val notificationListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notifications = ArrayList<Notification>()
                dataSnapshot.children.map {
                    notifications.add(
                        Notification(
                            key = it.key,
                            subject = it.getValue<Notification>()!!.subject,
                            message = it.getValue<Notification>()!!.message,
                            level = it.getValue<Notification>()!!.level,
                            timestamp = it.getValue<Notification>()!!.timestamp
                        )
                    )
                }

                val notificationAdapter = NotificationAdapter(notifications)
                binding.rvNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
                binding.rvNotification.setHasFixedSize(true)
                binding.rvNotification.adapter = notificationAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        refNotification.addValueEventListener(notificationListener)
    }
}