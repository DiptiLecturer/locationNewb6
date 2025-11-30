package org.freedu.minilocationb6

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.databinding.ActivityMyProfileBinding


class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var fusedLocation: FusedLocationProviderClient

    private var selectedUserId = ""
    private var selectedEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        selectedUserId = intent.getStringExtra("uid")!!
        selectedEmail = intent.getStringExtra("email")!!

        binding.email.text = selectedEmail

        val currentUid = auth.currentUser!!.uid
        if (currentUid != selectedUserId) binding.btnShare.visibility = View.GONE

        db.collection("users").document(selectedUserId).get()
            .addOnSuccessListener { doc ->
                val username = doc.getString("username") ?: ""
                binding.edtUsername.setText(username)
            }

        binding.btnUpdateUsername.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            if (username.isNotEmpty()) {
                db.collection("users").document(selectedUserId).update("username", username)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMap.setOnClickListener {
            val i = Intent(this, MapsActivity::class.java)
            i.putExtra("uid", selectedUserId)
            startActivity(i)
        }

        binding.btnShare.setOnClickListener { shareMyLocation() }
    }

    private fun shareMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        fusedLocation.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                db.collection("users").document(selectedUserId).update(
                    mapOf(
                        "latitude" to loc.latitude,
                        "longitude" to loc.longitude
                    )
                )
                Toast.makeText(this, "Location Updated!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}