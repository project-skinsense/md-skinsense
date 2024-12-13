package com.dicoding.picodiploma.loginwithanimation.view.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.SkinHealthRequest
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch

class QuickTestViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _submissionStatus = MutableLiveData<Result<Boolean>>()
    val submissionStatus: LiveData<Result<Boolean>> get() = _submissionStatus

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    fun getUserEmail() {
        viewModelScope.launch {
            _email.value = userRepository.getUserEmail()
        }
    }

    fun submitSkinHealth(email: String, skinHealthRequest: SkinHealthRequest) {
        viewModelScope.launch {
            try {
                _submissionStatus.value = Result.Loading
                val result = userRepository.submitSkinHealth(email, skinHealthRequest)
                _submissionStatus.value = Result.Success(result)
            } catch (e: Exception) {
                _submissionStatus.value = Result.Error(e)
            }
        }
    }
}

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
