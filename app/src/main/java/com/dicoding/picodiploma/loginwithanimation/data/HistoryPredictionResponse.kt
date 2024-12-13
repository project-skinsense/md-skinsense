package com.dicoding.picodiploma.loginwithanimation.data

data class HistoryPredictionResponse(
    val status: String,
    val message: String,
    val data: List<PredictionItem>
)

data class PredictionItem(
    val id: String,
    val userId: String,
    val result: String,
    val explanation: String,
    val suggestion: String,
    val confidenceScore: Int,
    val imageUrl: String,
    val createdAt: CreatedAt
)

data class CreatedAt(
    val _seconds: Long,
    val _nanoseconds: Long
)