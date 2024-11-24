package com.example.storyapps.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.storyapps.R
import com.example.storyapps.api.RegisterRequest
import com.example.storyapps.api.RegisterResponse
import com.example.storyapps.api.RetrofitClient
import com.example.storyapps.databinding.FragmentSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edRegisterName.setErrorTextView(binding.errorName)
        binding.edRegisterEmail.setErrorTextView(binding.errorEmail)
        binding.edRegisterPassword.setErrorTextView(binding.errorPassword)

        binding.btnSignup.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (validateCustomFields()) {
                registerUser(name, email, password)
            }
        }
    }

    private fun validateCustomFields(): Boolean {
        var isValid = true

        if (binding.edRegisterName.text.isNullOrEmpty()) {
            binding.errorName.text = "Nama harus diisi"
            isValid = false
        }

        if (binding.edRegisterEmail.text.isNullOrEmpty()) {
            binding.errorEmail.text = "Email harus diisi"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edRegisterEmail.text.toString()).matches()) {
            binding.errorEmail.text = "Format email tidak valid"
            isValid = false
        }

        val password = binding.edRegisterPassword.text.toString()
        if (password.isEmpty()) {
            binding.errorPassword.text = "Password harus diisi"
            isValid = false
        } else if (password.length < 8) {
            binding.errorPassword.text = "Password minimal 8 karakter"
            isValid = false
        } else if (!password.matches(Regex(".*[a-zA-Z].*"))) {
            binding.errorPassword.text = "Password harus mengandung huruf"
            isValid = false
        } else if (!password.matches(Regex(".*\\d.*"))) {
            binding.errorPassword.text = "Password harus mengandung angka"
            isValid = false
        }

        return isValid
    }

    private fun registerUser(name: String, email: String, password: String) {
        Log.d("SignupFragment", "Registering user: $name, $email")
        val request = RegisterRequest(name, email, password)
        val call = RetrofitClient.instance.registerUser(request)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()
                    if (registerResponse?.error == false) {
                        Toast.makeText(requireContext(), "Registrasi berhasil: ${registerResponse.message}", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.loginFragment)
                    } else {
                        Toast.makeText(requireContext(), "Gagal: ${registerResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal menghubungi server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
