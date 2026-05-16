package com.example.satwalaya.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.satwalaya.data.RegisterRequest
import com.example.satwalaya.data.RetrofitClient
import com.example.satwalaya.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToLoginText.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register via API
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.register(
                        request = RegisterRequest(
                            username = name,
                            password = password,
                            email = email,
                            phone = phone
                        )
                    )

                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val msg = response.body()?.message ?: "Registrasi gagal"
                        Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Tidak bisa terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}