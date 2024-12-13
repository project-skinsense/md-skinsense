package com.dicoding.picodiploma.loginwithanimation.data

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val otpSent: Boolean
)
