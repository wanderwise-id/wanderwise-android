package com.example.wanderwise.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wanderwise.data.database.HospitalPolice
import com.example.wanderwise.databinding.ListHospitalPoliceCardBinding

class EmergencyCallsAdapter(
    private val emergencyCall: ArrayList<HospitalPolice>
): RecyclerView.Adapter<EmergencyCallsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ListHospitalPoliceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val call = emergencyCall[position]
        holder.bind(call)

        holder.binding.callNumber.setOnClickListener {
            val callIntent: Intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${call.telp.toString()}"))
            holder.itemView.context.startActivity(callIntent)
        }
    }

    override fun getItemCount(): Int {
        return emergencyCall.size
    }

    class MyViewHolder(val binding: ListHospitalPoliceCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(emergencyCall: HospitalPolice) {
            binding.hospitalName.text = emergencyCall.name.toString()
            binding.address.text = emergencyCall.address.toString()
            binding.number.text = emergencyCall.telp.toString()
        }
    }
}