package com.example.storyapps.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.storyapps.R
import com.example.storyapps.api.LoginRequest
import com.example.storyapps.api.LoginResponse
import com.example.storyapps.api.RetrofitClient
import com.example.storyapps.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val moveLoopAnimation = AnimationUtils.loadAnimation(context, R.anim.move)
        binding.loginLogo.startAnimation(moveLoopAnimation)

        binding.loginEmail.setErrorTextView(binding.errorEmail)
        binding.loginPassword.setErrorTextView(binding.errorPassword)

        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (isInputValid(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        val isEmailValid = binding.errorEmail.text.isNullOrEmpty()
        val isPasswordValid = binding.errorPassword.text.isNullOrEmpty()

        if (!isEmailValid) {
            Toast.makeText(requireContext(), "Email tidak valid", Toast.LENGTH_SHORT).show()
        }
        if (!isPasswordValid) {
            Toast.makeText(requireContext(), "Password tidak valid", Toast.LENGTH_SHORT).show()
        }

        return isEmailValid && isPasswordValid
    }

    private fun loginUser(email: String, password: String) {
        Log.d("LoginFragment", "Logging in user: $email")
        val request = LoginRequest(email, password)
        val call = RetrofitClient.instance.loginUser(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()
                    if (loginResponse?.error == false) {
                        val sharedPreferences = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("auth_token", loginResponse.loginResult?.token).apply()

                        findNavController().navigate(R.id.dashboardFragment)
                    } else {
                        binding.errorPassword.text = "Gagal: ${loginResponse?.message}"
                        binding.errorPassword.visibility = View.VISIBLE
                    }
                } else {
                    binding.errorPassword.text = "Akun tidak ditemukan"
                    binding.errorPassword.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.errorPassword.text = "Gagal: ${t.message}"
                binding.errorPassword.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
