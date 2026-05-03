package com.example.satwalaya.data

data class Booking(
    val serviceName: String,
    val petName: String,
    val petType: String,
    val roomType: String,
    val startDate: String,
    val endDate: String,
    val nights: Int,
    val totalPrice: Int,
    var status: String = "Active"
)

object BookingStore {
    val bookings = mutableListOf<Booking>()

    fun addBooking(booking: Booking) {
        bookings.add(booking)
    }

    fun activeBookingsCount(): Int {
        return bookings.count { it.status == "Active" }
    }

    fun completedBookingsCount(): Int {
        return bookings.count { it.status == "Completed" }
    }

    fun totalBookingsCount(): Int {
        return bookings.size
    }

    fun totalPoints(): Int {
        return 250 + (bookings.size * 50)
    }
}