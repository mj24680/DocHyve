package com.example.dochyve.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dochyve.R
import com.example.dochyve.data.model.Patient
import com.example.dochyve.databinding.PatientItemBinding

class PatientAdapter : ListAdapter<Patient, PatientAdapter.PatientViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PatientViewHolder {
        val binding = PatientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PatientViewHolder, position: Int
    ) {
        val item = getItem(position)

        holder.binding.apply {
            tvName.text = item.name
            tvGender.text = "(${item.gender})"
            tvAge.text = "Age: ${item.age}"
            tvIssue.text = item.issue

            Glide.with(imgProfile.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.patient)
                .error(R.drawable.patient)
                .into(imgProfile)
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(
            oldItem: Patient, newItem: Patient
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Patient, newItem: Patient
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class PatientViewHolder(val binding: PatientItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
