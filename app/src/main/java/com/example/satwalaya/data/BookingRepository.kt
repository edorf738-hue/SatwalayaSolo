package com.example.satwalaya.data

import android.content.ContentValues
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepository(context: Context) {

    private val localDb = BookingDatabaseHelper(context)
    private val api = RetrofitClient.apiService

    // GET semua booking (offline-first)
    suspend fun getAllBookings(): List<Booking> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllBookings()
            if (response.isSuccessful && response.body()?.success == true) {
                val bookings = response.body()?.data?.map { it.toBooking() } ?: emptyList()
                syncLocalDatabase(bookings)
                bookings
            } else {
                getLocalBookings()
            }
        } catch (e: Exception) {
            getLocalBookings()
        }
    }

    // POST tambah booking
    suspend fun addBooking(booking: Booking): Boolean = withContext(Dispatchers.IO) {
        // Simpan ke lokal dulu
        localDb.insertBooking(booking)

        // Coba kirim ke API
        try {
            val request = BookingRequest(
                serviceName = booking.serviceName,
                petName = booking.petName,
                petType = booking.petType,
                roomType = booking.roomType,
                startDate = booking.startDate,
                endDate = booking.endDate,
                nights = booking.nights,
                totalPrice = booking.totalPrice,
                status = booking.status
            )
            val response = api.addBooking(request)
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            false
        }
    }

    // Sync data API ke SQLite lokal
    private fun syncLocalDatabase(bookings: List<Booking>) {
        val db = localDb.writableDatabase
        db.delete("bookings", null, null)
        bookings.forEach { booking ->
            localDb.insertBooking(booking)
        }
    }

    // Ambil dari SQLite lokal
    private fun getLocalBookings(): List<Booking> {
        return localDb.getActiveBookings()
    }

    // GET semua services dari API
    suspend fun getAllServices(): List<ServiceApiModel> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllServices()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}