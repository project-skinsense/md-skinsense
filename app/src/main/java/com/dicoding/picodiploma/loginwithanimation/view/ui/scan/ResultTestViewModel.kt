package com.dicoding.picodiploma.loginwithanimation.view.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.PredictResponse
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultTestViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _predictResult = MutableLiveData<PredictResponse>()
    val predictResult: LiveData<PredictResponse> get() = _predictResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadPrediction(file: MultipartBody.Part, userId: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.predictUpload(file, userId)
                _predictResult.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            _email.value = userRepository.getUserEmail()
        }
    }
}