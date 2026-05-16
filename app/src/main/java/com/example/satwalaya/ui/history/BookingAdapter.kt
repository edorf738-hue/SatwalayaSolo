package com.example.satwalaya.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.satwalaya.R
import com.example.satwalaya.data.Booking

class BookingAdapter(
    private var bookings: List<Booking>
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvServiceName: TextView = view.findViewById(R.id.tvServiceName)
        val tvPet: TextView = view.findViewById(R.id.tvPet)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnCancel: TextView = view.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val booking = bookings[position]

        holder.tvServiceName.text = booking.serviceName
        holder.tvPet.text = "${booking.petName} (${booking.petType})"
        holder.tvDate.text = "📅  ${booking.startDate} - ${booking.endDate}"
        holder.tvPrice.text = formatRupiah(booking.totalPrice)
        holder.tvStatus.text = booking.status
        holder.btnCancel.visibility = if (booking.status == "Active") View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = bookings.size

    fun updateData(newBookings: List<Booking>) {
        bookings = newBookings
        notifyDataSetChanged()
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }
}