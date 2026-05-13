package com.example.dochyve.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dochyve.R
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.model.DrawerMenuItem
import com.example.dochyve.data.remote.ApiService
import com.example.dochyve.data.remote.HttpClientFactory
import com.example.dochyve.data.repository.DashboardRepository
import com.example.dochyve.databinding.ActivityDashboardBinding
import com.example.dochyve.ui.appointments.AppointmentsFragment
import com.example.dochyve.ui.dashboard.adapters.DrawerMenuAdapter
import com.example.dochyve.ui.notifications.NotificationsFragment
import com.example.dochyve.ui.profile.ProfileFragment
import com.example.dochyve.ui.signin.SignInActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardActivityViewModel
    private lateinit var adapter: DrawerMenuAdapter
    private lateinit var tokenManager: TokenManager

    // Fragments
    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var appointmentsFragment: AppointmentsFragment
    private lateinit var notificationsFragment: NotificationsFragment
    private lateinit var profileFragment: ProfileFragment

    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // view models setup
        tokenManager = TokenManager(this)
        val apiService = ApiService(HttpClientFactory.create(tokenManager))
        val repository = DashboardRepository(apiService)

        viewModel = ViewModelProvider(
            this, DashboardActivityViewModelFactory(repository)
        )[DashboardActivityViewModel::class.java]

        setupFragments()
        setupUI()
        observeViewModel()

        if (savedInstanceState == null) {
            binding.bottomNav.selectedItemId = R.id.dashboard
        }
    }

    private fun setupUI() {
        binding.appbar.setNavigationOnClickListener {
            binding.main.openDrawer(GravityCompat.START)
        }

        binding.llLogout.setOnClickListener {
            performLogout()
        }

        setupDrawerRecyclerView()
        setupBottomNavigation()
    }

    fun performLogout() {
        val tokenManager = TokenManager(this)
        tokenManager.clearToken()
        // clear session using SessionManager(this).clearSession()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupDrawerRecyclerView() {

        adapter = DrawerMenuAdapter(

            // just for normal click either child or parent
            onItemClick = { item ->
                handleMenuClick(item)
            },

            // for expand and collapse sub-items
            onToggleClick = { item ->
                viewModel.toggleItem(item)
            })

        binding.rvDrawerMenu.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = this@DashboardActivity.adapter
        }
    }

    private fun observeViewModel() {

        viewModel.menuList.observe(this) {
            adapter.submitList(it) // flattened list
        }

        viewModel.currentTitle.observe(this) {
            binding.appbar.title = it
        }
    }

    private fun handleMenuClick(item: DrawerMenuItem) {

        // for now, navigation based on title. later using route coming from API
        when (item.title.lowercase()) {
            "dashboard" -> binding.bottomNav.selectedItemId = R.id.dashboard
            "appointments" -> binding.bottomNav.selectedItemId = R.id.appointments
            "notifications" -> binding.bottomNav.selectedItemId = R.id.notifications
            "profile" -> binding.bottomNav.selectedItemId = R.id.profile
        }

        binding.main.closeDrawer(GravityCompat.START)
    }

    private fun setupFragments() {

        dashboardFragment = DashboardFragment()
        appointmentsFragment = AppointmentsFragment()
        notificationsFragment = NotificationsFragment()
        profileFragment = ProfileFragment()

        activeFragment = dashboardFragment

        supportFragmentManager.beginTransaction().add(R.id.fl_main, dashboardFragment, "DASHBOARD")
            .add(R.id.fl_main, appointmentsFragment, "APPOINTMENT").hide(appointmentsFragment)
            .add(R.id.fl_main, notificationsFragment, "NOTIFICATION").hide(notificationsFragment)
            .add(R.id.fl_main, profileFragment, "PROFILE").hide(profileFragment).commit()
    }

    private fun switchFragment(target: Fragment) {

        if (activeFragment == target) return

        activeFragment?.let { current ->
            supportFragmentManager.beginTransaction().setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out
            ).hide(current).show(target).commit()
        }

        activeFragment = target
    }

    private fun setupBottomNavigation() {

        binding.bottomNav.setOnItemSelectedListener { item ->

            val title = when (item.itemId) {
                R.id.dashboard -> "Dashboard"
                R.id.appointments -> "Appointments"
                R.id.notifications -> "Notifications"
                R.id.profile -> "Profile"
                else -> ""
            }

            if (title.isEmpty()) return@setOnItemSelectedListener false

            viewModel.updateTitle(title)

            when (item.itemId) {
                R.id.dashboard -> switchFragment(dashboardFragment)
                R.id.appointments -> switchFragment(appointmentsFragment)
                R.id.notifications -> switchFragment(notificationsFragment)
                R.id.profile -> switchFragment(profileFragment)
            }

            true
        }
    }
}