package com.example.satwalaya.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.satwalaya.data.LoginRequest
import com.example.satwalaya.data.RetrofitClient
import com.example.satwalaya.databinding.ActivityLoginBinding
import com.example.satwalaya.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("satwalaya_prefs", MODE_PRIVATE)

        // Kalau udah login, langsung masuk
        if (prefs.getBoolean("is_logged_in", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login via API
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.login(
                        request = LoginRequest(username = email, password = password)
                    )

                    if (response.isSuccessful && response.body()?.success == true) {
                        val user = response.body()?.data

                        // Simpan session di SharedPreferences
                        prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putString("logged_in_user", user?.email ?: email)
                            .putString("user_name", user?.username ?: "Pengguna")
                            .putInt("user_id", user?.id ?: 0)
                            .apply()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        val msg = response.body()?.message ?: "Login gagal"
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}