package com.mevi.tarantula.utils

import com.mevi.tarantula.iu.login.LoginViewModel

object Utilities {

    fun isAdmin(loginViewModel: LoginViewModel, email: String): Boolean {
        val adminEmails = loginViewModel.adminEmails.value
        return adminEmails.contains(email)
    }

}