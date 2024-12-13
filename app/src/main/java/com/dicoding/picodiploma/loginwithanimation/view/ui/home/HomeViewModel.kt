package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Prediction
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.HistoryPredictionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _predictions = MutableLiveData<Result<List<Prediction>>>()
    val predictions: LiveData<Result<List<Prediction>>> = _predictions

    private val _isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isLoading: LiveData<Boolean> = _isLoading

    fun getPredictions(email: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentPredictions = _predictions.value?.getOrNull() ?: emptyList()

                val responseResult = repository.getPrediction(email)

                if (responseResult.isSuccess) {
                    val prediction = responseResult.getOrNull()

                    val predictions = prediction?.data?.map { predictionItem ->
                        Prediction(
                            id = predictionItem.id,
                            userId = predictionItem.userId,
                            result = predictionItem.result,
                            explanation = predictionItem.explanation,
                            suggestion = predictionItem.suggestion,
                            confidenceScore = predictionItem.confidenceScore.toDouble(),
                            imageUrl = predictionItem.imageUrl,
                            createdAt = predictionItem.createdAt
                        )
                    }

                    if (predictions != null && predictions != currentPredictions) {
                        _predictions.postValue(Result.success(predictions))
                    } else if (currentPredictions.isEmpty()) {
                        _predictions.postValue(Result.success(predictions ?: emptyList()))
                    }

                } else {
                    _predictions.postValue(Result.failure(responseResult.exceptionOrNull() ?: Exception("Unknown error")))
                }
            } catch (e: Exception) {
                _predictions.postValue(Result.failure(e))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            _email.value = repository.getUserEmail()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }

    private fun HistoryPredictionEntity.toPrediction(): Prediction {
        return Prediction(
            id = this.id.toString(),
            userId = this.userId,
            result = this.result,
            explanation = this.explanation,
            suggestion = this.suggestion,
            confidenceScore = this.confidenceScore,
            imageUrl = this.imageUrl,
            createdAt = this.createdAt
        )
    }

}