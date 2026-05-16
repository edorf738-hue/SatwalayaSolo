package com.example.satwalaya.ui.booking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.satwalaya.R
import com.example.satwalaya.data.Booking
import com.example.satwalaya.data.BookingRepository
import com.example.satwalaya.databinding.ActivityPaymentBinding
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var selectedPaymentMethod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalPrice = intent.getIntExtra("totalPrice", 0)
        binding.tvTotalPrice.text = formatRupiah(totalPrice)

        binding.cardBri.setOnClickListener {
            selectedPaymentMethod = "BRI"
            binding.cardBri.setBackgroundResource(R.drawable.bg_payment_selected)
            binding.cardGopay.setBackgroundResource(R.drawable.bg_grooming_input)
        }

        binding.cardGopay.setOnClickListener {
            selectedPaymentMethod = "GoPay"
            binding.cardGopay.setBackgroundResource(R.drawable.bg_payment_selected)
            binding.cardBri.setBackgroundResource(R.drawable.bg_grooming_input)
        }

        binding.btnConfirmPayment.setOnClickListener {
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Pilih metode pembayaran dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val booking = Booking(
                serviceName = intent.getStringExtra("serviceName") ?: "Hotel Service",
                petName = intent.getStringExtra("petName") ?: "-",
                petType = intent.getStringExtra("petType") ?: "-",
                roomType = intent.getStringExtra("roomType") ?: "-",
                startDate = intent.getStringExtra("startDate") ?: "-",
                endDate = intent.getStringExtra("endDate") ?: "-",
                nights = intent.getIntExtra("nights", 0),
                totalPrice = totalPrice,
                status = "Active"
            )

            val repository = BookingRepository(this)

            lifecycleScope.launch {
                val success = repository.addBooking(booking)

                val intent = Intent(this@PaymentActivity, BookingConfirmedActivity::class.java)
                intent.putExtra("totalPrice", totalPrice)
                intent.putExtra("paymentMethod", selectedPaymentMethod)
                startActivity(intent)
                finish()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }
}