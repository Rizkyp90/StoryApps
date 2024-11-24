package com.example.storyapps.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyapps.R
import com.example.storyapps.adapter.LoadingStateAdapter
import com.example.storyapps.adapter.StoryAdapter
import com.example.storyapps.api.RetrofitClient
import com.example.storyapps.viewmodel.StoryViewModel
import com.example.storyapps.viewmodel.ViewModelFactory
import com.example.storyapps.helper.StoryRepository


class DashboardFragment : Fragment() {

    private val viewModel: StoryViewModel by viewModels { 
        ViewModelFactory(StoryRepository(RetrofitClient.instance))
    }

    private lateinit var storyRecyclerView: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var textNoInternet: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "StoryApps"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        setHasOptionsMenu(true)

        storyRecyclerView = view.findViewById(R.id.recycler_view_stories)
        storyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        progressBar = view.findViewById(R.id.progress_bar)
        textNoInternet = view.findViewById(R.id.text_no_internet)

        if (isUserLoggedIn()) {
            setupAdapter()
            observePagedData()
        } else {
            navigateToLogin()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (isUserLoggedIn()) {
            refreshData()
        }
    }

    private fun refreshData() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token != null) {
            viewModel.refreshStories()
            viewModel.getStories(token).observe(viewLifecycleOwner) { pagingData ->
                storyAdapter.submitData(lifecycle, pagingData)
            }
        }
    }

    private fun setupAdapter() {
        storyAdapter = StoryAdapter { story ->
            val bundle = Bundle().apply {
                putString("storyId", story.id)
            }
            findNavController().navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
        }
        storyRecyclerView.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
    }

    private fun observePagedData() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token != null) {
            viewModel.getStories(token).observe(viewLifecycleOwner) { pagingData ->
                storyAdapter.submitData(lifecycle, pagingData)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_story -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_addStoryFragment)
                true
            }
            R.id.action_logout -> {
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains("auth_token")
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.welcomeFragment)
    }

    private fun logoutUser() {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("auth_token").apply()
        navigateToLogin()
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
