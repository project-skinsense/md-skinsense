package com.dicoding.picodiploma.loginwithanimation.data.api

import com.dicoding.picodiploma.loginwithanimation.data.ApiResponse
import com.dicoding.picodiploma.loginwithanimation.data.HistoryPredictionResponse
import com.dicoding.picodiploma.loginwithanimation.data.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.OtpResponse
import com.dicoding.picodiploma.loginwithanimation.data.PredictResponse
import com.dicoding.picodiploma.loginwithanimation.data.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.SkinHealthRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("verify-otp")
    suspend fun verifyOtp(
        @Field("email") email: String,
        @Field("otp") otp: Int
    ): Response<OtpResponse>

    @POST("user/{email}/skin-health")
    suspend fun submitSkinHealth(
        @Path("email") email: String,
        @Body request: SkinHealthRequest
    ): Response<ApiResponse>

    @Multipart
    @POST("predict-upload")
    suspend fun predictUpload(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody
    ): Response<PredictResponse>

    @GET("predict/{email}")
    fun getPredictions(
        @Path("email") email: String
    ): Call<HistoryPredictionResponse>
}
