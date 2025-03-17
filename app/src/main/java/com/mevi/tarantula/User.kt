package com.mevi.tarantula

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class User : Application() {
    override fun onCreate() {
        super.onCreate()

    }
    companion object{
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        var userInvited: Boolean = false
        var userAdmin: Boolean = false
        var userId: String = ""
        var userName: String = ""
        var userEmail: String = ""
        var userPhone: String = ""
    }

}