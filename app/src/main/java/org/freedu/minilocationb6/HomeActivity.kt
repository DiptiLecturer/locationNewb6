package org.freedu.minilocationb6

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: FirebaseFirestore
    private val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.userRecycler.layoutManager = LinearLayoutManager(this)

        loadUsers()
    }

    private fun loadUsers() {
        db.collection("users").addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener

            userList.clear()

            for (doc in value!!) {
                val user = doc.toObject(User::class.java)
                userList.add(user)
            }

            binding.userRecycler.adapter =
                UserAdapter(userList) { selectedUser ->
                    val intent = Intent(this, UserDetails::class.java)
                    intent.putExtra("uid", selectedUser.userId)
                    intent.putExtra("email", selectedUser.email)
                    startActivity(intent)
                }
        }
    }
}
