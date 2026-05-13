package com.example.dochyve.ui.signin

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dochyve.data.repository.AuthRepository
import com.example.dochyve.data.repository.UiState
import kotlinx.coroutines.launch

class SignInViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableLiveData<UiState>()
    val state: LiveData<UiState> = _state

    fun login(email: String, password: String) {
        val validationError = validateCredentials(email, password)
        if (validationError != null) {
            _state.value = UiState.Error(validationError)
            return
        }
        viewModelScope.launch {
            _state.value = UiState.Loading
            val result = repository.login(email, password)
            result.onSuccess {
                _state.value = UiState.Success(it.message)
            }.onFailure {
                _state.value = UiState.Error(
                    if (it.message == "fail") "Invalid Credentials" else "Unable to connect. Please check your internet and try again."
                )
            }
        }
    }

    private fun validateCredentials(email: String, password: String): String? {
        return when {
            email.isBlank() && password.isBlank() -> "Email and Password are required"
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address"
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters long"
            else -> null
        }
    }
}
