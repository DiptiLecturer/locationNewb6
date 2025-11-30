package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.repo.UserRepository

class AuthViewModel(private val repo: UserRepository) : ViewModel() {

    val loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult = MutableLiveData<Pair<Boolean, String?>>()

    fun login(email: String, password: String) {
        repo.loginUser(email, password) { success, msg ->
            loginResult.value = success to msg
        }
    }

    fun register(email: String, password: String) {
        repo.registerUser(email, password) { success, msg ->
            registerResult.value = success to msg
        }
    }
}
