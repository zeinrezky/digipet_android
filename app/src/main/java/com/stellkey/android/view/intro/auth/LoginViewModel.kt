package com.stellkey.android.view.intro.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.AllProfileModel
import com.stellkey.android.model.LoginModel
import com.stellkey.android.model.request.CarerLoginRequest
import com.stellkey.android.model.request.KidLoginRequest
import com.stellkey.android.model.request.LoginRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val loginResponse = MutableLiveData<LoginModel>()
    val carerLoginSuccess = SingleLiveEvent<Unit>()
    val loginFailed = SingleLiveEvent<Unit>()

    val allProfileSelection = MutableLiveData<AllProfileModel>()

    fun postLogin(loginRequest: LoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postRegisterLogin(loginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    loginResponse.value = response.body.data.let { response.body.data }
                }
                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                }
                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                }

                else -> {}
            }
        }
    }

    fun postCarerLogin(carerLoginRequest: CarerLoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postCarerLogin(carerLoginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    carerLoginSuccess.call()
                }
                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    loginFailed.call()
                }
                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                }

                else -> {}
            }
        }
    }

    fun postKidLogin(kidLoginRequest: KidLoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postKidLogin(kidLoginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    loginResponse.value = response.body.data.let { response.body.data }
                }
                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    loginFailed.call()
                }
                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                }

                else -> {}
            }
        }
    }

    fun getAllProfileSelection(loginToken: String) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getAllProfileSelection(loginToken)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    allProfileSelection.value = response.body.data
                }
                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                }
                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                }

                else -> {}
            }
        }
    }

}