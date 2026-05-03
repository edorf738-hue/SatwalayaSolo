package com.example.satwalaya.ui.booking

import android.os.Bundle
import android.view.View
import com.example.satwalaya.R
import androidx.appcompat.app.AppCompatActivity
import com.example.satwalaya.databinding.ActivityPickupBookingBinding

class PickupBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickupBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPickupBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvPickupDateTwo.setOnClickListener {
            binding.sectionPickupTime.visibility = View.VISIBLE

            binding.tvPickupDateTwo.setBackgroundResource(R.drawable.bg_date_selected)
            binding.tvPickupDateTwo.setTextColor(getColor(android.R.color.white))
        }

        binding.tvPickupTimeEight.setOnClickListener {
            binding.tvPickupTimeEight.setBackgroundResource(R.drawable.bg_time_selected)
            binding.tvPickupTimeEight.setTextColor(getColor(android.R.color.white))
        }
    }
}