package com.example.wanderwise.ui.rank

import android.content.ContentValues.TAG
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.wanderwise.R
import com.example.wanderwise.data.database.City
import com.example.wanderwise.data.database.LocationCity
import com.example.wanderwise.data.database.Score
import com.example.wanderwise.ui.adapter.CityExploreAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class RankMapsFragment : Fragment() {
    private lateinit var mMap: GoogleMap

    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap

        val rankSatu = BitmapDescriptorFactory.fromResource(R.drawable.rank1)
        val rankDua = BitmapDescriptorFactory.fromResource(R.drawable.rank2)
        val rankTiga = BitmapDescriptorFactory.fromResource(R.drawable.rank3)

        val db = FirebaseDatabase.getInstance("https://wanderwise-application-default-rtdb.asia-southeast1.firebasedatabase.app")

        val refCities = db.getReference("cities")
        val refScores = db.getReference("scores")

        val cityListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount > 0) {

                    dataSnapshot.children.map { citySnapshot ->
                        val city = citySnapshot.getValue<City>()
                        val location = citySnapshot.child("location").getValue<LocationCity>()

                        if (city != null && location != null) {

                            val cityKey = citySnapshot.key
                            val scoreLast: MutableMap<String, Int> = mutableMapOf()

                            refScores.child(cityKey.toString()).limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(scoreSnapshot: DataSnapshot) {
                                    if (scoreSnapshot.childrenCount > 0){
                                        scoreSnapshot.children.forEach{
                                            if(it.getValue<Score>()!!.score != null){
                                                scoreLast[cityKey.toString()] = it.getValue<Score>()!!.score.toString().toDouble().toInt()
                                            } else{
                                                scoreLast[cityKey.toString()] = 0
                                            }
                                        }

                                        val sortedEntries = scoreLast.entries.sortedByDescending { it.value }
                                        val top3Entries = sortedEntries.take(3)

                                        top3Entries.forEach() {
                                            val markerLatLng = LatLng(location.lat.toString().toDouble(), location.lon.toString().toDouble())

                                            Log.d("isiTop3Entries", "${it.key} and ${it.value}")

                                            mMap.addMarker(
                                                MarkerOptions()
                                                    .position(markerLatLng)
                                                    .title(it.key)
                                                    .snippet(it.value.toString())
                                                    .icon(rankSatu)
                                            ).also { marker ->
                                                if (marker != null) {
                                                    marker.tag = it.key
                                                }
                                            }
                                        }

                                        val bounds: LatLngBounds = boundsBuilder.build()
                                        mMap.animateCamera(
                                            CameraUpdateFactory.newLatLngBounds(
                                                bounds,
                                                resources.displayMetrics.widthPixels,
                                                resources.displayMetrics.heightPixels,
                                                300
                                            )
                                        )
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.w("TAG", "Failed to read score value.", error.toException())
                                }
                            })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read city value.", error.toException())
            }
        }
        refCities.addValueEventListener(cityListener)

        setMapStyle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_rank_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),
                    R.raw.map_style
                ))
            if (!success) {
                Log.e(TAG, getString(R.string.map_parsing))
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, getString(R.string.map), exception)
        }
    }
}