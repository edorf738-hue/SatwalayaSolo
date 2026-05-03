package com.example.satwalaya.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.satwalaya.R
import com.example.satwalaya.ui.auth.LoginActivity
import com.example.satwalaya.data.BookingStore
import com.example.satwalaya.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileBinding.bind(view)

        // Baca nama & email dari SharedPreferences
        val prefs = requireContext().getSharedPreferences("satwalaya_prefs", android.content.Context.MODE_PRIVATE)
        val name = prefs.getString("user_name", "Pengguna")
        val email = prefs.getString("user_email", "-")
        binding.tvProfileName.text = name
        binding.tvProfileEmail.text = email

        binding.tvTotalBookings.text = BookingStore.totalBookingsCount().toString()
        binding.tvCompleted.text = BookingStore.completedBookingsCount().toString()
        binding.tvPoints.text = BookingStore.totalPoints().toString()

        binding.btnLogout.setOnClickListener {
            // Hapus data saat logout
            prefs.edit().clear().apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}