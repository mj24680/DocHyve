package com.example.dochyve.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dochyve.data.model.Notification
import com.example.dochyve.databinding.NotificationItemBinding

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHodler>(NotificationDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHodler {
        val binding =
            NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHodler(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHodler,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHodler(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification) {
            binding.tvDescription.text =
                "A New Appointment has been booked by ${item.patientName} with ${item.doctorName} for ${item.date} at ${item.time}."
            binding.tvDuration.text = item.duration
        }
    }
}

class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem == newItem
    }

}
