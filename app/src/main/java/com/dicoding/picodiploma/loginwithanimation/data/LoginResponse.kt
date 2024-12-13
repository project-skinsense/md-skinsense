package com.dicoding.picodiploma.loginwithanimation.data

data class LoginResponse(
    val message: String,
    val user: UserData
)

data class UserData(
    val name: String,
    val email: String
)
