package com.mevi.tarantula.utils

import com.mevi.tarantula.iu.login.LoginViewModel

object Utilities {

    fun isAdmin(loginViewModel: LoginViewModel, email: String): Boolean {
        val adminEmails = loginViewModel.adminEmails.value
        return adminEmails.contains(email)
    }

    fun getDirectDriveImageUrl(originalUrl: String): String {
        val regex = Regex("/d/([a-zA-Z0-9_-]+)")
        val match = regex.find(originalUrl)
        val id = match?.groupValues?.get(1)
        return if (id != null) {
            "https://drive.google.com/uc?export=download&id=$id"
        } else {
            originalUrl // fallback
        }
    }
}