package com.example.storyapps.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.storyapps.R
import com.example.storyapps.api.RetrofitClient
import com.example.storyapps.api.StoryResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        loadStoriesWithLocation()
    }

    private fun loadStoriesWithLocation() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = "Bearer ${sharedPreferences.getString("auth_token", "")}"

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getStories(token = token, location = 1)

                if (response.isSuccessful) {
                    val storyResponse = response.body()
                    if (storyResponse != null) {
                        addMarkersToMap(storyResponse)
                    } else {
                        Log.e("MapsFragment", "Response body is null")
                    }
                } else {
                    Log.e("MapsFragment", "Failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MapsFragment", "Error loading stories with location", e)
            }
        }
    }

    private fun addMarkersToMap(storyResponse: StoryResponse) {
        storyResponse.listStory?.forEach { story ->
            val lat = story?.latitude ?: 0.0
            val lon = story?.longitude ?: 0.0
            val position = LatLng(lat, lon)

            val markerOptions = MarkerOptions()
                .position(position)
                .title(story?.name)
                .snippet(story?.description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            mMap.addMarker(markerOptions)
        }

        if (storyResponse.listStory?.isNotEmpty() == true) {
            val firstStory = storyResponse.listStory.first()
            val firstLat = firstStory?.latitude ?: 0.0
            val firstLon = firstStory?.longitude ?: 0.0
            val firstPosition = LatLng(firstLat, firstLon)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, 5f))
        }
    }

}
