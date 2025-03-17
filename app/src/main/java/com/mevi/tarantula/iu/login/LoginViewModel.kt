package com.mevi.tarantula.iu.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = enableLogin(email = email, password = password)
    }

    fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6
/*
    fun onLoginSelected() {
        viewModelScope.launch {
            _isLoading.value = true
            val request = LoginRequest(email = email.value ?: "", password = password.value ?: "")
            val result = loginUseCase(request)
            if (result != null) {
                Log.i("Alex", "Login exitoso: ${result.token}")
            } else {
                Log.e("Alex", "Error en login: respuesta nula")
            }
            _isLoading.value = false

        }
    }

 */
}