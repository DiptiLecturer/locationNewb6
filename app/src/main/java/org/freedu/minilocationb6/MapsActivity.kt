package org.freedu.minilocationb6

import android.content.Intent
import android.os.Bundle
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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val userId = auth.currentUser!!.uid

        db.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                val lat = doc.getDouble("latitude")
                val lng = doc.getDouble("longitude")

                if (lat != null && lng != null) {
                    val pos = LatLng(lat, lng)

                    map.addMarker(
                        MarkerOptions().position(pos).title("Last Shared Location")
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
                }
            }
    }
}