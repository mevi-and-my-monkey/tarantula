package com.mevi.tarantula.core

sealed class AuthState {
    object Idle : AuthState() // Estado inicial
    object Loading : AuthState() // Cargando
    object Success : AuthState() // Autenticación exitosa
    data class Error(val message: String) : AuthState() // Error con mensaje
}