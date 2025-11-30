package org.freedu.minilocationb6.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.freedu.minilocationb6.repo.UserRepository

class MyProfileViewModel(private val repo: UserRepository) : ViewModel() {

    val usernameUpdateResult = MutableLiveData<Boolean>()

    fun updateUsername(userId: String, username: String) {
        repo.updateUsername(userId, username) { success ->
            usernameUpdateResult.value = success
        }
    }

    fun shareLocation(userId: String, lat: Double, lng: Double) {
        repo.updateLocation(userId, lat, lng)
    }
}
