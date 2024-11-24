package com.example.storyapps.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.api.RetrofitClient
import com.example.storyapps.api.DetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryFragment : Fragment() {

    private lateinit var photoImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var createdAtTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var locationTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_story, container, false)

        photoImageView = view.findViewById(R.id.photoImageView)
        nameTextView = view.findViewById(R.id.nameTextView)
        createdAtTextView = view.findViewById(R.id.createdAtTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        locationTextView = view.findViewById(R.id.locationTextView)

        val storyId = arguments?.getString("storyId") ?: ""
        fetchStoryDetail(storyId)

        return view
    }

    private fun fetchStoryDetail(storyId: String) {
        val token = "Bearer " + (requireContext().getSharedPreferences("auth_prefs", 0)
            .getString("auth_token", "") ?: "")

        RetrofitClient.instance.getStoryDetail(storyId, token).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    response.body()?.story?.let { story ->
                        nameTextView.text = story.name
                        createdAtTextView.text = story.createdAt
                        descriptionTextView.text = story.description
                        locationTextView.text = "Lat: ${story.lat}, Lon: ${story.lon}"

                        Glide.with(this@DetailStoryFragment)
                            .load(story.photoUrl)
                            .into(photoImageView)
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat cerita", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
