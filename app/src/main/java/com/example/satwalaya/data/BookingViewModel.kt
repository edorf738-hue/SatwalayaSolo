package com.example.satwalaya.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> = _bookings

    private val _services = MutableLiveData<List<ServiceApiModel>>()
    val services: LiveData<List<ServiceApiModel>> = _services

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadBookings()
        loadServices()
    }

    fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _bookings.value = repository.getAllBookings()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat booking: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadServices() {
        viewModelScope.launch {
            try {
                _services.value = repository.getAllServices()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat services: ${e.message}"
            }
        }
    }

    fun addBooking(booking: Booking) {
        viewModelScope.launch {
            repository.addBooking(booking)
            loadBookings()
        }
    }
}