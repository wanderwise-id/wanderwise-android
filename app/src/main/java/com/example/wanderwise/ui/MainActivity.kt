package com.example.wanderwise.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wanderwise.R
import com.example.wanderwise.data.preferences.UserModel
import com.example.wanderwise.databinding.ActivityMainBinding
import com.example.wanderwise.ui.home.HomeFragment
import com.example.wanderwise.ui.profile.ProfileViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.postFragment, R.id.rankMapsFragment, R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.homeFragment)
                    true
                }
                R.id.rankMapsFragment -> {
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.rankMapsFragment)
                    true
                }
                R.id.postFragment -> {
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.postFragment)
                    true
                }
                R.id.profileFragment -> {
                    findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check for permission and request if not granted
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location-log", "permission granted")
            requestLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        supportActionBar?.hide()
    }

    private fun requestLocationUpdates() {
        //Check if the necessary location permissions are granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location-log", "Permission not granted")
            return
        }

        //Request location updates
        fusedLocationClient.requestLocationUpdates(
            getLocationRequest(),
            locationCallback,
            null
        )
    }

    private fun getLocationRequest(): LocationRequest {
        var interval = 6000*5
        profileViewModel.getSessionUser().observe(this@MainActivity) {
            if (it.userLocation.isEmpty()) {
                interval = 0
            }
        }
        return LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(interval.toLong()) //Update location every 5 minutes
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            Log.d("location-log", "Location: $location")

            // Now you have the latitude and longitude
            val latitude = location?.latitude
            val longitude = location?.longitude
            Log.d("location-log", "Latitude: $latitude, Longitude: $longitude")

            if (latitude != null && longitude != null) {
                getCityNameFromLatLng(latitude, longitude)
            }
        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            // Handle location availability changes if needed
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                requestLocationUpdates()
            } else {
                // Permission denied, handle accordingly
                Log.d("location-log","Location permission denied")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)

        if (currentFragment is HomeFragment) {
            finishAffinity()
        }
    }

    private fun getCityNameFromLatLng(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                val cityName = addresses[0].subAdminArea.replace(Regex("(Kota|City|Kabupaten)"), "").replace(Regex("\\s+"), "")
                Log.d("LocationCityName", "City Name: $cityName")

                profileViewModel.getSessionUser().observe(this@MainActivity) { user ->
                    profileViewModel.editUserModel(UserModel(
                        name = user.name,
                        token = user.token,
                        email = user.email,
                        uid = user.uid,
                        userLocation = cityName,
                        currentActivity = "profile",
                        isLogin = true
                    ))
                }

                //Store to DataStore : store the variable caled cityName to datastore
            } else {
                Log.d("Location", "No address found")
            }
        } catch (e: IOException) {
            Log.e("Location", "Error getting city name", e)
        }
        return null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}