package com.example.dochyve.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dochyve.R
import com.example.dochyve.data.model.Date
import com.example.dochyve.databinding.DateItemBinding

class DatePickerAdapter(
    private val onDateClick: (Date) -> Unit
) : ListAdapter<Date, DatePickerAdapter.DateViewHolder>(DiffCallback()) {

    private var selectedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = DateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = position == selectedItemPosition

        holder.bind(item, isSelected)

        holder.itemView.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            if (clickedPosition == RecyclerView.NO_POSITION) return@setOnClickListener
            if (clickedPosition == selectedItemPosition) return@setOnClickListener

            val previousPosition = selectedItemPosition
            selectedItemPosition = clickedPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedItemPosition)

            onDateClick(getItem(clickedPosition))
        }
    }

    fun setSelectedPosition(position: Int) {
        if (position == selectedItemPosition) return

        val oldPosition = selectedItemPosition
        selectedItemPosition = position

        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedItemPosition)
    }

    fun getSelectedDate(): Date {
        return getItem(selectedItemPosition)
    }

    inner class DateViewHolder(
        private val binding: DateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Date, isSelected: Boolean) {
            binding.apply {
                txtDay.text = item.dayName
                txtDate.text = item.dayNumber

                updateItemUI(this, isSelected)
            }
        }
    }

    private fun updateItemUI(binding: DateItemBinding, isSelected: Boolean) {
        val context = binding.root.context

        if (isSelected) {
            binding.main.setBackgroundColor(
                ContextCompat.getColor(context, R.color.primary_blue)
            )
            binding.txtDay.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
            binding.txtDate.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            binding.main.setBackgroundResource(android.R.color.transparent)
            binding.txtDay.setTextColor(
                ContextCompat.getColor(context, R.color.black)
            )
            binding.txtDate.setTextColor(
                ContextCompat.getColor(context, R.color.black)
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Date>() {
        override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
            return oldItem.dayNumber == newItem.dayNumber &&
                    oldItem.dayName == newItem.dayName
        }

        override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
            return oldItem == newItem
        }
    }
}