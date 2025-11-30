package org.freedu.minilocationb6

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.freedu.minilocationb6.databinding.ActivityAuthBinding
import org.freedu.minilocationb6.databinding.ActivityMapsBinding
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val userId = intent.getStringExtra("uid") ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                val lat = doc.getDouble("latitude")
                val lng = doc.getDouble("longitude")
                val username = doc.getString("username")
                val email = doc.getString("email")

                if (lat != null && lng != null) {
                    val pos = LatLng(lat, lng)

                    // Use username if available, else email
                    val title = if (!username.isNullOrEmpty()) username else email ?: "User"

                    map.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(title)
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load location", Toast.LENGTH_SHORT).show()
            }
    }
}
