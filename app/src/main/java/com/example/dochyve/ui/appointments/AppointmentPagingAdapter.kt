package com.example.dochyve.ui.appointments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dochyve.R
import com.example.dochyve.data.Constants
import com.example.dochyve.data.model.Appointment
import com.example.dochyve.databinding.AppointmentRequestItemBinding
import com.example.dochyve.ui.appointments.AppointmentDiffCallback
import java.security.interfaces.RSAMultiPrimePrivateCrtKey

class AppointmentPagingAdapter(
    private val onAcceptClick:(Appointment)-> Unit,
    private val onRejectClick:(Appointment) -> Unit
) :
    PagingDataAdapter<Appointment, AppointmentPagingAdapter.AppointmentViewHolder>(
        AppointmentDiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppointmentViewHolder {
        val binding = AppointmentRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AppointmentViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class AppointmentViewHolder(private val binding: AppointmentRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Appointment) {
            binding.tvDescription.text =
                "${item.patient.firstname} ${item.patient.lastname} has requested an appointment with ${item.provider.firstname} ${item.provider.lastname} on ${item.date_formatted} at ${item.time_formatted}"
            binding.tvDuration.text = item.time_formatted

            val imageUrl = item.patient.profile_image
            if(!imageUrl.isNullOrEmpty()){
                Glide.with(binding.ivPatient.context)
                    .load(Constants.BASE_URL_STORAGE + imageUrl)
                    .placeholder(R.drawable.iv_placeholder)
                    .error(R.drawable.iv_placeholder)
                    .into(binding.ivPatient)
            }else{
                binding.ivPatient.setImageResource(R.drawable.iv_placeholder)
            }

            binding.btnAccept.setOnClickListener {
                onAcceptClick(item)
            }

            binding.btnReject.setOnClickListener {
                onRejectClick(item)
            }
        }

    }
}

class AppointmentDiffCallback : DiffUtil.ItemCallback<Appointment>(){
    override fun areItemsTheSame(
        oldItem: Appointment,
        newItem: Appointment
    ): Boolean {
        return oldItem.appointment_id == newItem.appointment_id
    }

    override fun areContentsTheSame(
        oldItem: Appointment,
        newItem: Appointment
    ): Boolean {
        return oldItem == newItem
    }

}