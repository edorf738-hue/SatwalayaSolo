package com.example.satwalaya.ui.history

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.satwalaya.R
import com.example.satwalaya.data.BookingDatabaseHelper
import com.example.satwalaya.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)

        showActive()

        binding.tabActive.setOnClickListener { showActive() }
        binding.tabCompleted.setOnClickListener { showCompleted() }
        binding.tabCancelled.setOnClickListener { showCancelled() }
    }

    private fun resetTabs() {
        binding.tabActive.background = null
        binding.tabCompleted.background = null
        binding.tabCancelled.background = null

        binding.tabActive.setTypeface(null, Typeface.NORMAL)
        binding.tabCompleted.setTypeface(null, Typeface.NORMAL)
        binding.tabCancelled.setTypeface(null, Typeface.NORMAL)
    }

    private fun showActive() {
        resetTabs()
        binding.tabActive.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabActive.setTypeface(null, Typeface.BOLD)

        val db = BookingDatabaseHelper(requireContext())
        val bookings = db.getActiveBookings()

        if (bookings.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.bookingListContainer.visibility = View.GONE

            binding.emptyIcon.text = "🗓️"
            binding.emptyTitle.text = "No active bookings"
            binding.emptySubtitle.text = "Book a service to get started!"
            binding.browseServiceButton.visibility = View.VISIBLE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.bookingScrollView.visibility = View.VISIBLE

            binding.bookingListContainer.removeAllViews()

            for (booking in bookings) {
                val card = layoutInflater.inflate(
                    R.layout.item_booking,
                    binding.bookingListContainer,
                    false
                )

                val tvServiceName = card.findViewById<TextView>(R.id.tvServiceName)
                val tvPet = card.findViewById<TextView>(R.id.tvPet)
                val tvDate = card.findViewById<TextView>(R.id.tvDate)
                val tvPrice = card.findViewById<TextView>(R.id.tvPrice)
                val tvStatus = card.findViewById<TextView>(R.id.tvStatus)
                val btnCancel = card.findViewById<TextView>(R.id.btnCancel)

                tvServiceName.text = booking.serviceName
                tvPet.text = "${booking.petName} (${booking.petType})"
                tvDate.text = "📅  ${booking.startDate} - ${booking.endDate}"
                tvPrice.text = formatRupiah(booking.totalPrice)
                tvStatus.text = "Confirmed"
                btnCancel.visibility = View.VISIBLE

                binding.bookingListContainer.addView(card)
            }
        }
    }

    private fun showCompleted() {
        resetTabs()
        binding.tabCompleted.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCompleted.setTypeface(null, Typeface.BOLD)

        binding.bookingScrollView.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        binding.emptyIcon.text = "✅"
        binding.emptyTitle.text = "No completed bookings"
        binding.emptySubtitle.text = "Your completed bookings will appear here"
        binding.browseServiceButton.visibility = View.GONE
    }

    private fun showCancelled() {
        resetTabs()
        binding.tabCancelled.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCancelled.setTypeface(null, Typeface.BOLD)

        binding.bookingListContainer.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        binding.emptyIcon.text = "❌"
        binding.emptyTitle.text = "No cancelled bookings"
        binding.emptySubtitle.text = "Great! Keep it that way!"
        binding.browseServiceButton.visibility = View.GONE
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}