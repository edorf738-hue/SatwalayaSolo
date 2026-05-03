package com.example.satwalaya.ui.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.satwalaya.R
import com.example.satwalaya.data.Booking
import com.example.satwalaya.data.BookingStore
import com.example.satwalaya.databinding.ActivityPetHotelBookingBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class PetHotelBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetHotelBookingBinding

    private var selectedRoom = "Basic Room"
    private var selectedPrice = 50000

    private var checkInMillis: Long = 0
    private var checkOutMillis: Long = 0
    private var totalNights = 0

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetHotelBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        binding.cardBasicRoom.setOnClickListener { selectRoom("Basic Room", 50000) }
        binding.cardPremiumRoom.setOnClickListener { selectRoom("Premium Room", 100000) }
        binding.cardVipRoom.setOnClickListener { selectRoom("VIP Suite", 200000) }

        binding.checkInCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            checkInMillis = getMillis(year, month, dayOfMonth)
            checkOutMillis = 0
            totalNights = 0

            binding.checkInText.text = "Check-in: ${dateFormat.format(Date(checkInMillis))}"
            binding.checkOutContainer.visibility = View.VISIBLE
            binding.nightInfoText.text = "Pilih tanggal check-out"
            binding.priceSummaryCard.visibility = View.GONE

            updateConfirmButton()
        }

        binding.confirmBookingButton.setOnClickListener {
            val petName = binding.petNameInput.text.toString().trim()
            val petType = binding.petTypeInput.text.toString().trim()

            if (petName.isEmpty() || petType.isEmpty()) {
                Toast.makeText(this, "Pet name dan pet type wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalPrice = selectedPrice * totalNights

            BookingStore.addBooking(
                Booking(
                    serviceName = "Hotel Service",
                    petName = petName,
                    petType = petType,
                    roomType = selectedRoom,
                    startDate = dateFormat.format(Date(checkInMillis)),
                    endDate = dateFormat.format(Date(checkOutMillis)),
                    nights = totalNights,
                    totalPrice = totalPrice,
                    status = "Active"
                )
            )

            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("serviceName", "Hotel Service")
            intent.putExtra("petName", petName)
            intent.putExtra("petType", petType)
            intent.putExtra("roomType", selectedRoom)
            intent.putExtra("startDate", dateFormat.format(Date(checkInMillis)))
            intent.putExtra("endDate", dateFormat.format(Date(checkOutMillis)))
            intent.putExtra("nights", totalNights)
            intent.putExtra("totalPrice", totalPrice)
            startActivity(intent)
            finish()
        }

        binding.checkOutCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            checkOutMillis = getMillis(year, month, dayOfMonth)

            if (checkOutMillis <= checkInMillis) {
                Toast.makeText(this, "Check-out harus setelah check-in", Toast.LENGTH_SHORT).show()
                checkOutMillis = 0
                totalNights = 0
                binding.nightInfoText.text = "Pilih tanggal check-out"
                binding.priceSummaryCard.visibility = View.GONE
            } else {
                totalNights = TimeUnit.MILLISECONDS.toDays(checkOutMillis - checkInMillis).toInt()
                binding.nightInfoText.text =
                    "$totalNights night • ${dateFormat.format(Date(checkInMillis))} - ${dateFormat.format(
                        Date(checkOutMillis)
                    )}"

                updatePriceSummary()
            }

            updateConfirmButton()
        }
    }

    private fun selectRoom(roomName: String, price: Int) {
        selectedRoom = roomName
        selectedPrice = price

        binding.cardBasicRoom.setBackgroundResource(R.drawable.bg_room_card)
        binding.cardPremiumRoom.setBackgroundResource(R.drawable.bg_room_card)
        binding.cardVipRoom.setBackgroundResource(R.drawable.bg_room_card)

        binding.basicTitle.text = "Basic Room"
        binding.premiumTitle.text = "Premium Room"
        binding.vipTitle.text = "VIP Suite"

        when (roomName) {
            "Basic Room" -> {
                binding.cardBasicRoom.setBackgroundResource(R.drawable.bg_room_card_selected)
                binding.basicTitle.text = "Basic Room  ✓"
            }
            "Premium Room" -> {
                binding.cardPremiumRoom.setBackgroundResource(R.drawable.bg_room_card_selected)
                binding.premiumTitle.text = "Premium Room  ✓"
            }
            "VIP Suite" -> {
                binding.cardVipRoom.setBackgroundResource(R.drawable.bg_room_card_selected)
                binding.vipTitle.text = "VIP Suite  ✓"
            }
        }

        if (totalNights > 0) {
            updatePriceSummary()
        }
    }

    private fun updatePriceSummary() {
        val totalPrice = selectedPrice * totalNights

        binding.priceSummaryCard.visibility = View.VISIBLE
        binding.summaryRoom.text = "Room ($selectedRoom)"
        binding.summaryRoomPrice.text = formatRupiah(selectedPrice)
        binding.summaryNight.text = "$totalNights night"
        binding.summaryNightCount.text = "× $totalNights"
        binding.summaryTotal.text = formatRupiah(totalPrice)
    }

    private fun updateConfirmButton() {
        val isReady = checkInMillis > 0 && checkOutMillis > 0 && totalNights > 0

        binding.confirmBookingButton.isEnabled = isReady
        binding.confirmBookingButton.alpha = if (isReady) 1f else 0.55f
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }

    private fun getMillis(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}