package com.example.satwalaya.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Booking endpoints
    @GET("bookings.php")
    suspend fun getAllBookings(): Response<ApiResponse<List<BookingApiModel>>>

    @POST("bookings.php")
    suspend fun addBooking(
        @Body booking: BookingRequest
    ): Response<ApiResponse<Map<String, Int>>>

    @PUT("bookings.php")
    suspend fun updateBookingStatus(
        @Query("id") id: Int,
        @Body status: Map<String, String>
    ): Response<ApiResponse<Unit>>

    @DELETE("bookings.php")
    suspend fun deleteBooking(
        @Query("id") id: Int
    ): Response<ApiResponse<Unit>>

    // Service endpoints
    @GET("services.php")
    suspend fun getAllServices(): Response<ApiResponse<List<ServiceApiModel>>>

    @GET("services.php")
    suspend fun getServicesByCategory(
        @Query("category") category: String
    ): Response<ApiResponse<List<ServiceApiModel>>>

    @POST("auth.php")
    suspend fun login(
        @Query("action") action: String = "login",
        @Body request: LoginRequest
    ): Response<ApiResponse<UserApiModel>>

    @POST("auth.php")
    suspend fun register(
        @Query("action") action: String = "register",
        @Body request: RegisterRequest
    ): Response<ApiResponse<Map<String, Int>>>
}