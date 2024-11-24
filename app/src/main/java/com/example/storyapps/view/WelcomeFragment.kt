package com.example.storyapps.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.storyapps.R
import com.example.storyapps.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isUserLoggedIn()) {
            findNavController().navigate(R.id.dashboardFragment)
            return
        }

        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in)

        binding.imgWelcome.startAnimation(fadeIn)
        binding.tvWelcomeMessage.startAnimation(slideIn)
        binding.tvAppDescription.startAnimation(slideIn)
        binding.btnGoToLogin.startAnimation(slideIn)
        binding.btnGoToSignup.startAnimation(slideIn)

        binding.btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.btnGoToSignup.setOnClickListener {
            findNavController().navigate(R.id.signupFragment)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        return !token.isNullOrEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
