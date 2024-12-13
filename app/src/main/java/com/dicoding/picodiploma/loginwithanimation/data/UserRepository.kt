package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.HistoryPredictionEntity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserDao
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val userDao: UserDao, private val apiService: ApiService) {

    suspend fun loginUser(email: String, password: String): UserEntity {
        val response = apiService.login(email, password)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                val user = UserEntity(
                    email = body.user.email,
                    name = body.user.name,
                    isLogin = true
                )
                userDao.insertUserSession(user)
                return user
            }
        }
        throw Exception("Login failed: ${response.message()}")
    }

    suspend fun getUserSession(): UserEntity? {
        return userDao.getUserSession()
    }

    suspend fun getUserEmail(): String? {
        return userDao.getEmail()
    }

    suspend fun getUserName(): String? {
        return userDao.getName()
    }

    suspend fun logoutUser() {
        userDao.clearSession()
    }

    suspend fun registerUser(email: String, password: String, name: String): Boolean {
        val response = apiService.register(email, password, name)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.message == "OTP sent to email") {
                    return true
                } else {
                    throw Exception("OTP not sent: ${body.message}")
                }
            } ?: throw Exception("Empty response body.")
        } else {
            throw Exception("Registration failed: ${response.message()}")
        }
    }

    suspend fun verifyOtp(email: String, otp: Int): Boolean {
        val response = apiService.verifyOtp(email, otp)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.message == "Registration successful") {
                    return true
                } else {
                    throw Exception("OTP verification failed: ${body.message}")
                }
            } ?: throw Exception("Empty response body.")
        } else {
            throw Exception("OTP verification failed: ${response.message()}")
        }
    }

    suspend fun submitSkinHealth(email: String, skinHealthRequest: SkinHealthRequest): Boolean {
        val response = apiService.submitSkinHealth(email, skinHealthRequest)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.message == "Skin health data saved successfully for user") {
                    return true
                } else {
                    throw Exception("Submission failed: ${body.message}")
                }
            } ?: throw Exception("Empty response body.")
        } else {
            throw Exception("API submission failed: ${response.message()}")
        }
    }

    suspend fun predictUpload(file: MultipartBody.Part, userId: RequestBody): PredictResponse {
        val response = apiService.predictUpload(file, userId)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                return body
            } ?: throw Exception("Empty response body.")
        } else {
            throw Exception("Prediction upload failed: ${response.message()}")
        }
    }

    suspend fun getPrediction(email: String): Result<HistoryPredictionResponse> {
        val call = apiService.getPredictions(email)

        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()?.let { historyPredictionResponse ->
                    historyPredictionResponse.data.forEach { predictionItem ->
                        val historyPredictionEntity = predictionItem.toHistoryPredictionEntity()

                        val existingPrediction = userDao.getPredictionById(historyPredictionEntity.id.toString())
                        if (existingPrediction == null) {
                            userDao.insertHistoryPrediction(historyPredictionEntity)
                        } else {
                            Log.d("Database", "Data with id ${historyPredictionEntity.id} already exists")
                        }
                    }
                    Result.success(historyPredictionResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun PredictionItem.toHistoryPredictionEntity(): HistoryPredictionEntity {
        return HistoryPredictionEntity(
            userId = this.userId,
            result = this.result,
            explanation = this.explanation,
            suggestion = this.suggestion,
            confidenceScore = this.confidenceScore.toDouble(),
            imageUrl = this.imageUrl,
            createdAt = this.createdAt.toString()
        )
    }

    suspend fun getAllHistoryPredictions(): List<HistoryPredictionEntity> {
        return userDao.getAllHistoryPredictions()
    }

}
