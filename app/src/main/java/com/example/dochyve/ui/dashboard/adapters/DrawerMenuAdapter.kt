package com.example.dochyve.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dochyve.data.model.DrawerMenuItem
import com.example.dochyve.databinding.DrawerMenuItemBinding

class DrawerMenuAdapter(
    private val onItemClick: (DrawerMenuItem) -> Unit,
    private val onToggleClick: (DrawerMenuItem) -> Unit
) : ListAdapter<DrawerMenuItem, DrawerMenuAdapter.MenuViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = DrawerMenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MenuViewHolder(
        private val binding: DrawerMenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DrawerMenuItem) {
            binding.tvTitle.text = item.title

            if(item.iconUrl != null){ // show/load menu item icon
                binding.icMenuItem.visibility = View.VISIBLE
                Glide.with(binding.icMenuItem).load(item.iconUrl).into(binding.icMenuItem)
            }else{
                binding.icMenuItem.visibility = View.INVISIBLE
            }

            binding.icArrow.visibility = if(item.isExpandable) View.VISIBLE else View.GONE // handle dropdown arrow visibility
            binding.icArrow.rotation = if(item.isExpanded) 180f else 0f

            binding.root.setOnClickListener {
                if(item.isExpandable){
                    onToggleClick(item)
                } else {
                    onItemClick(item)
                }
            }

            binding.icArrow.setOnClickListener {
                onToggleClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DrawerMenuItem>() {

        override fun areItemsTheSame(oldItem: DrawerMenuItem, newItem: DrawerMenuItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DrawerMenuItem, newItem: DrawerMenuItem): Boolean {
            return oldItem == newItem
        }
    }
}