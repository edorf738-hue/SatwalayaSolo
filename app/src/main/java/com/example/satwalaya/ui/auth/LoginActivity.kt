package com.example.satwalaya.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.satwalaya.databinding.ActivityLoginBinding
import com.example.satwalaya.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("satwalaya_prefs", MODE_PRIVATE)

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

            val savedEmail = prefs.getString("user_email", null)
            val savedPassword = prefs.getString("user_password", null)

            if (savedEmail == null || savedPassword == null) {
                Toast.makeText(this, "Akun tidak ditemukan. Silakan register dulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email != savedEmail || password != savedPassword) {
                Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("logged_in_user", email)
                .apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}