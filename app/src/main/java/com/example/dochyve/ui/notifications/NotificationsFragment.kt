package com.example.dochyve.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dochyve.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding : FragmentNotificationsBinding
    private lateinit var viewModel: NotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerview()
        observerNotifications()
    }

    private fun setupRecyclerview() {
        viewModel = NotificationViewModel()
        viewModel.notification.observe(viewLifecycleOwner){list ->
            notificationAdapter.submitList(list)
        }
    }

    private fun observerNotifications() {
        notificationAdapter = NotificationAdapter()
        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
    }
}