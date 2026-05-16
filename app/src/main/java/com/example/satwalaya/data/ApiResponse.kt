package com.example.satwalaya.data

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)

data class BookingApiModel(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("pet_name") val petName: String,
    @SerializedName("pet_type") val petType: String,
    @SerializedName("room_type") val roomType: String?,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("nights") val nights: Int,
    @SerializedName("total_price") val totalPrice: Int,
    @SerializedName("status") val status: String
) {
    fun toBooking(): Booking = Booking(
        serviceName = serviceName,
        petName = petName,
        petType = petType,
        roomType = roomType ?: "",
        startDate = startDate,
        endDate = endDate,
        nights = nights,
        totalPrice = totalPrice,
        status = status
    )
}

data class ServiceApiModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Int
)

data class UserApiModel(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String = ""
)

data class BookingRequest(
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("service_name") val serviceName: String,
    @SerializedName("pet_name") val petName: String,
    @SerializedName("pet_type") val petType: String,
    @SerializedName("room_type") val roomType: String = "",
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("nights") val nights: Int = 0,
    @SerializedName("total_price") val totalPrice: Int,
    @SerializedName("status") val status: String = "Active"
)