package com.dicoding.picodiploma.loginwithanimation.view.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel()  {

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email
    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> get() = _name

    fun getUserEmail() {
        viewModelScope.launch {
            _email.value = userRepository.getUserEmail()
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            _name.value = userRepository.getUserName()
        }
    }
}