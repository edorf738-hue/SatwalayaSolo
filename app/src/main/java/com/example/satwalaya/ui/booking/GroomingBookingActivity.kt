package com.example.satwalaya.ui.booking

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.satwalaya.R
import com.example.satwalaya.databinding.ActivityGroomingBookingBinding

class GroomingBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroomingBookingBinding

    private var isServiceSelected = false
    private var isDateSelected = false
    private var isTimeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroomingBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.etPetName.addTextChangedListener {
            checkFormValid()
        }

        binding.etPetType.addTextChangedListener {
            checkFormValid()
        }

        binding.tvDateOne.setOnClickListener {
            isDateSelected = true

            binding.sectionTimeSlot.visibility = View.VISIBLE
            binding.tvDateOne.setBackgroundResource(R.drawable.bg_date_selected)
            binding.tvDateOne.setTextColor(getColor(android.R.color.white))

            checkFormValid()
        }

        binding.tvTimeNine.setOnClickListener {
            isTimeSelected = true

            binding.tvTimeNine.setBackgroundResource(R.drawable.bg_time_selected)
            binding.tvTimeNine.setTextColor(getColor(android.R.color.white))

            checkFormValid()
        }

        binding.cardBath.setOnClickListener {
            isServiceSelected = true

            binding.cardBath.setBackgroundResource(R.drawable.bg_grooming_card_selected)
            binding.cardHaircut.setBackgroundResource(R.drawable.bg_grooming_card)
            binding.cardNail.setBackgroundResource(R.drawable.bg_grooming_card)
            binding.cardSpa.setBackgroundResource(R.drawable.bg_grooming_card)

            binding.cardPriceSummary.visibility = View.VISIBLE
            binding.tvSelectedServiceSummary.text = "Bath & Blow Dry"
            binding.tvServicePrice.text = "Rp 50.000"
            binding.tvTotalPrice.text = "Total: Rp 50.000"

            checkFormValid()
        }

        binding.cardHaircut.setOnClickListener {
            isServiceSelected = true
            Toast.makeText(this, "Haircut & Styling dipilih", Toast.LENGTH_SHORT).show()
            checkFormValid()
        }

        binding.cardNail.setOnClickListener {
            isServiceSelected = true
            Toast.makeText(this, "Nail Trimming dipilih", Toast.LENGTH_SHORT).show()
            checkFormValid()
        }

        binding.cardSpa.setOnClickListener {
            isServiceSelected = true
            Toast.makeText(this, "Full Spa Package dipilih", Toast.LENGTH_SHORT).show()
            checkFormValid()
        }

        binding.btnConfirmBooking.setOnClickListener {
            startActivity(Intent(this, BookingConfirmedActivity::class.java))
            finish()
        }

        binding.btnConfirmBooking.setOnClickListener {
            binding.btnConfirmBooking.alpha = 0.8f

            startActivity(Intent(this, BookingConfirmedActivity::class.java))
            finish()
        }
    }

    private fun checkFormValid() {
        val petName = binding.etPetName.text.toString().trim()
        val petType = binding.etPetType.text.toString().trim()

        val isValid = petName.isNotEmpty()
                && petType.isNotEmpty()
                && isServiceSelected
                && isDateSelected
                && isTimeSelected

        binding.btnConfirmBooking.isEnabled = isValid
        binding.btnConfirmBooking.alpha = if (isValid) 1f else 0.5f
    }
}