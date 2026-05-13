package com.example.dochyve.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dochyve.data.datastore.SessionManager
import com.example.dochyve.data.datastore.TokenManager
import com.example.dochyve.data.remote.ApiService
import com.example.dochyve.data.remote.HttpClientFactory
import com.example.dochyve.data.repository.AuthRepository
import com.example.dochyve.data.repository.UiState
import com.example.dochyve.databinding.ActivitySignInBinding
import com.example.dochyve.ui.dashboard.DashboardActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tokenManager = TokenManager(this)
        val sessionManager = SessionManager(this)
        val client = HttpClientFactory.create(tokenManager)
        val apiService = ApiService(client)
        val repository = AuthRepository(apiService, tokenManager, sessionManager)

        viewModel = SignInViewModel(repository)

        autofillEmail(sessionManager)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.loginError.btnOk.setOnClickListener {
            binding.loginError.loginErrorLayout.visibility = View.GONE
        }
    }

    private fun autofillEmail(sessionManager: SessionManager){
        val savedEmail = sessionManager.getEmail()
        if(!savedEmail.isNullOrEmpty()){
            binding.etEmail.setText(savedEmail)
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state -> // default -> it
            when (state) {
                is UiState.Loading -> showLoading(true)
                is UiState.Success -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    goToDashboard()
                }
                is UiState.Error -> {
                    showLoading(false)
                    showErrorDialog(state.message)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loader.loaderLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorDialog(message: String) {
        binding.loginError.apply {
            loginErrorLayout.visibility = View.VISIBLE
            tvMessage.text = message
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
