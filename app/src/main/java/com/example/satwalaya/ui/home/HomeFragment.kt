package com.example.satwalaya.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.satwalaya.ui.booking.PetHotelBookingActivity
import com.example.satwalaya.R
import com.example.satwalaya.data.BookingRepository
import com.example.satwalaya.data.BookingViewModel
import com.example.satwalaya.data.BookingViewModelFactory
import com.example.satwalaya.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)

        // Setup ViewModel
        val repository = BookingRepository(requireContext())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BookingViewModel::class.java]

        // Baca nama dari SharedPreferences
        val prefs = requireContext().getSharedPreferences("satwalaya_prefs", android.content.Context.MODE_PRIVATE)
        val name = prefs.getString("user_name", "Pengguna")
        binding.tvUserName.text = name

        // Observe bookings buat cek active booking
        viewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            val activeBookings = bookings.filter { it.status == "Active" }

            if (activeBookings.isNotEmpty()) {
                binding.cardActiveBooking.visibility = View.VISIBLE
            } else {
                binding.cardActiveBooking.visibility = View.GONE
            }
        }

        binding.cardContinuePayment.visibility = View.GONE

        binding.cardPetHotel.setOnClickListener {
            startActivity(Intent(requireContext(), PetHotelBookingActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}