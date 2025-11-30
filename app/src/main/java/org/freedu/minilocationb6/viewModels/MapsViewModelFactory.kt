package org.freedu.minilocationb6.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

class MapsViewModelFactory(private val db: FirebaseFirestore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
