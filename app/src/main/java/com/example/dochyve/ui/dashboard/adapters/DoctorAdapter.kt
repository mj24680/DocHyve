package com.example.dochyve.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dochyve.R
import com.example.dochyve.data.model.Doctor
import com.example.dochyve.databinding.DoctorItemBinding

class DoctorAdapter :
    ListAdapter<Doctor, DoctorAdapter.DoctorViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = DoctorItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = getItem(position)
        holder.bind(doctor)
    }

    inner class DoctorViewHolder(
        private val binding: DoctorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(doctor: Doctor) {
            binding.apply {
                tvDoctorName.text = doctor.name
                tvSpeciality.text = "Speciality: ${doctor.speciality}"
                tvRole.text = "Role: ${doctor.role}"
                ivDoctor.setImageResource(doctor.imageRes)

                updateStatusUI(doctor.isActive)
            }
        }

        private fun updateStatusUI(isActive: Boolean) {
            val context = binding.root.context

            if (isActive) {
                binding.tvStatus.text = "● Active"
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.green)
                )
            } else {
                binding.tvStatus.text = "● Inactive"
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(context, R.color.light_gray)
                )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Doctor>() {
        override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean {
            return oldItem == newItem
        }
    }
}