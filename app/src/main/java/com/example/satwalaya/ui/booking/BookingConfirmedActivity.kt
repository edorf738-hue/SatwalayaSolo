package com.example.satwalaya.ui.booking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.satwalaya.ui.main.MainActivity
import com.example.satwalaya.databinding.ActivityBookingConfirmedBinding

class BookingConfirmedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingConfirmedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingConfirmedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalPrice = intent.getIntExtra("totalPrice", 50000)
        binding.confirmedPrice.text = formatRupiah(totalPrice)

        binding.viewHistoryButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openHistory", true)
            startActivity(intent)
            finish()
        }

        binding.backHomeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }
}