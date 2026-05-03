package com.example.satwalaya.ui.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.satwalaya.R
import com.example.satwalaya.databinding.FragmentBookingBinding

class BookingFragment : Fragment(R.layout.fragment_booking) {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentBookingBinding.bind(view)

        binding.cardPetHotelBooking.setOnClickListener {
            startActivity(Intent(requireContext(), PetHotelBookingActivity::class.java))
        }

        binding.cardGroomingBooking.setOnClickListener {
            startActivity(Intent(requireContext(), GroomingBookingActivity::class.java))
        }

        binding.cardPickupBooking.setOnClickListener {
            startActivity(Intent(requireContext(), PickupBookingActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}