package org.freedu.minilocationb6

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnShare.setOnClickListener {
            shareLocation()
        }

        binding.btnMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun shareLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        fusedLocation.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val userId = auth.currentUser!!.uid

                val updateMap = mapOf(
                    "latitude" to loc.latitude,
                    "longitude" to loc.longitude
                )

                db.collection("users").document(userId).update(updateMap)

                Toast.makeText(this, "Location Shared!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}