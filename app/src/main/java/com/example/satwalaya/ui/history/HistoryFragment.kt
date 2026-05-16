package com.example.satwalaya.ui.history

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satwalaya.R
import com.example.satwalaya.data.Booking
import com.example.satwalaya.data.BookingRepository
import com.example.satwalaya.data.BookingViewModel
import com.example.satwalaya.data.BookingViewModelFactory
import com.example.satwalaya.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BookingViewModel
    private lateinit var adapter: BookingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)

        // Setup ViewModel
        val repository = BookingRepository(requireContext())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BookingViewModel::class.java]

        // Setup RecyclerView
        adapter = BookingAdapter(emptyList())
        binding.rvBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookings.adapter = adapter

        // Observe booking data
        viewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            showBookingList(bookings.filter { it.status == "Active" })
        }

        // Observe error
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // Tab click listeners
        binding.tabActive.setOnClickListener { showActive() }
        binding.tabCompleted.setOnClickListener { showCompleted() }
        binding.tabCancelled.setOnClickListener { showCancelled() }

        // Default tab
        showActiveTab()
    }

    private fun showActiveTab() {
        resetTabs()
        binding.tabActive.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabActive.setTypeface(null, Typeface.BOLD)
    }

    private fun showActive() {
        showActiveTab()
        val allBookings = viewModel.bookings.value ?: emptyList()
        showBookingList(allBookings.filter { it.status == "Active" })
    }

    private fun showCompleted() {
        resetTabs()
        binding.tabCompleted.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCompleted.setTypeface(null, Typeface.BOLD)

        val allBookings = viewModel.bookings.value ?: emptyList()
        showBookingList(allBookings.filter { it.status == "Completed" })
    }

    private fun showCancelled() {
        resetTabs()
        binding.tabCancelled.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCancelled.setTypeface(null, Typeface.BOLD)

        val allBookings = viewModel.bookings.value ?: emptyList()
        showBookingList(allBookings.filter { it.status == "Cancelled" })
    }

    private fun showBookingList(bookings: List<Booking>) {
        if (bookings.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.rvBookings.visibility = View.GONE

            binding.emptyIcon.text = "🗓️"
            binding.emptyTitle.text = "No bookings found"
            binding.emptySubtitle.text = "Book a service to get started!"
            binding.browseServiceButton.visibility = View.VISIBLE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.rvBookings.visibility = View.VISIBLE
            adapter.updateData(bookings)
        }
    }

    private fun resetTabs() {
        binding.tabActive.background = null
        binding.tabCompleted.background = null
        binding.tabCancelled.background = null

        binding.tabActive.setTypeface(null, Typeface.NORMAL)
        binding.tabCompleted.setTypeface(null, Typeface.NORMAL)
        binding.tabCancelled.setTypeface(null, Typeface.NORMAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}