package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityOtpVerificationBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private val otpVerificationViewModel by viewModels<OtpVerificationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val progressBar= binding.progressBar
        val verifyButton = binding.verifyOtpButton

        otpVerificationViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                verifyButton.isEnabled = false
            } else {
                progressBar.visibility = View.GONE
                verifyButton.isEnabled = true
            }
        }
        enableEdgeToEdge()
        val email = intent.getStringExtra("email") ?: ""

        binding.verifyOtpButton.setOnClickListener {
            val otp = listOf(
                binding.otpDigit1.text.toString(),
                binding.otpDigit2.text.toString(),
                binding.otpDigit3.text.toString(),
                binding.otpDigit4.text.toString(),
                binding.otpDigit5.text.toString(),
                binding.otpDigit6.text.toString()
            ).joinToString("")

            if (otp.length == 6 && otp.all { it.isDigit() }) {
                val otpInteger = otp.toInt()

                verifyOtp(email, otpInteger)
            } else {
                Toast.makeText(this, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
            }
        }
        setOtpDigitWatchers()
    }

    private fun setOtpDigitWatchers() {
        val digits = listOf(
            binding.otpDigit1,
            binding.otpDigit2,
            binding.otpDigit3,
            binding.otpDigit4,
            binding.otpDigit5,
            binding.otpDigit6
        )

        for (i in 0 until digits.size - 1) {
            digits[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length == 1) {
                        digits[i + 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun verifyOtp(email: String, otp: Int) {
        otpVerificationViewModel.verifyOtp(email, otp,
            onSuccess = {
                Toast.makeText(this, "OTP Verified Successfully! Please Login", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },
            onError = { errorMessage ->
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        )
    }
}