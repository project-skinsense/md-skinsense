package com.dicoding.picodiploma.loginwithanimation.data.pref

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_prediction")
data class HistoryPredictionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val result: String,
    val explanation: String,
    val suggestion: String,
    val confidenceScore: Double,
    val imageUrl: String,
    val createdAt: String
)
