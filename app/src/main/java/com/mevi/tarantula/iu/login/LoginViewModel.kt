package com.mevi.tarantula.iu.login

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.*
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.mevi.tarantula.User
import com.mevi.tarantula.core.AuthState
import com.mevi.tarantula.network.UserModel


@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val aut = Firebase.auth
    private val firestore = Firebase.firestore

    var isLoading by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoginEnable by mutableStateOf(false)
        private set

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    private val _adminEmails = mutableStateOf(listOf(""))
    val adminEmails: State<List<String>> = _adminEmails

    fun setAdminEmails(emails: List<String>?) {
        _adminEmails.value = emails ?: listOf("")
    }

    fun signUp(name: String, email: String, password: String, phone:String, onResult: (Boolean, String?) -> Unit) {
        isLoading = true
        aut.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                isLoading = false
                val userid = it.result?.user?.uid
                val userModel = UserModel(name, email, userid!!, phone)
                firestore.collection("users").document(userid).set(userModel)
                    .addOnCompleteListener { response->
                    if (response.isSuccessful) {
                            onResult(true, null)
                        } else {
                            onResult(false, "Something went wrong")
                        }
                    }
            } else {
                isLoading = false
                onResult(false, it.exception?.localizedMessage)
            }
        }
    }

    fun onLoginChanged(email: String, password: String) {
        this.email = email
        this.password = password
        isLoginEnable = enableLogin(email = email, password = password)
    }

    private fun enableLogin(email: String, password: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6



    private fun firebaseAuthConGoogle(idToken: String) {
        val auth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                User.userId = user?.uid.toString()
                user?.let {
                }
            } else {
                task.exception?.let {
                    println("Error en la autenticación con Firebase: ${it.message}")
                }
            }
        }
    }
}