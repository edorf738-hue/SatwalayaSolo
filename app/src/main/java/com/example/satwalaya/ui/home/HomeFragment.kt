package com.example.satwalaya.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.satwalaya.ui.booking.PetHotelBookingActivity
import com.example.satwalaya.R
import com.example.satwalaya.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        // Baca nama dari SharedPreferences
        val prefs = requireContext().getSharedPreferences("satwalaya_prefs", android.content.Context.MODE_PRIVATE)
        val name = prefs.getString("user_name", "Pengguna")
        binding.tvUserName.text = name

        val hasPendingPayment = false // sementara dummy dulu

        if (hasPendingPayment) {
            binding.cardContinuePayment.visibility = View.VISIBLE
        } else {
            binding.cardContinuePayment.visibility = View.GONE
        }

        val hasActiveBooking = false // sementara dummy dulu

        if (hasActiveBooking) {
            binding.cardActiveBooking.visibility = View.VISIBLE
        } else {
            binding.cardActiveBooking.visibility = View.GONE
        }

        binding.cardPetHotel.setOnClickListener {
            startActivity(Intent(requireContext(), PetHotelBookingActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}