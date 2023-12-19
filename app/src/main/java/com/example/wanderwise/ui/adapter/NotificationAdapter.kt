package com.example.wanderwise.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.wanderwise.R
import com.example.wanderwise.data.database.Notification
import com.example.wanderwise.databinding.ListNotificationCardBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class NotificationAdapter (
    private val notifications: ArrayList<Notification>
): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){
    class MyViewHolder (val binding: ListNotificationCardBinding) : RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(notification: Notification) {

            var formatedDate = ""
            var duration = 0
            if (notification.timestamp != null){
                // Date formating
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = Date(notification.timestamp as Long)
                formatedDate = sdf.format(date)

                // Time formating
                val currentDT = LocalDateTime.ofInstant(
                    Instant.now(),
                    ZoneId.systemDefault())
                val notifcationDT = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(notification.timestamp as Long),
                    ZoneId.systemDefault())

                duration = Duration.between(notifcationDT, currentDT).toHours().toInt()
            }
            binding.dateNotif.text = formatedDate.toString()
            binding.timeNotif.text = "${duration.toString()} h"

            binding.descriptionNotif.text = if (notification.subject != null) "${notification.subject}" else "-"
            binding.imagePreviewNotification.setImageResource(R.drawable.danger_icon_large)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.MyViewHolder {
        val binding = ListNotificationCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationAdapter.MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}