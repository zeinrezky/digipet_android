package com.stellkey.android.view.intro.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.AllProfileModel
import com.stellkey.android.model.Carer
import com.stellkey.android.model.Kid
import com.stellkey.android.model.LoginModel
import com.stellkey.android.model.request.CarerLoginRequest
import com.stellkey.android.model.request.ForgotPinCarerRequest
import com.stellkey.android.model.request.ForgotPinKidRequest
import com.stellkey.android.model.request.KidLoginRequest
import com.stellkey.android.model.request.LoginRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.Constant.ResponseStatusCode.OK
import com.stellkey.android.util.Constant.ResponseStatusCode.USER_BANNED
import com.stellkey.android.util.Constant.ResponseStatusCode.USER_NOT_FOUND
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val loginResponse = MutableLiveData<LoginModel>()
    val loginFailed = SingleLiveEvent<Unit>()
    val successKidForgotPin = SingleLiveEvent<Unit>()
    val successCarerForgotPin = SingleLiveEvent<Unit>()

    val allProfileSelection = MutableLiveData<AllProfileModel>()
    val carerLoginSuccess = MutableLiveData<Carer>()
    val kidLoginSuccess = MutableLiveData<Kid>()

    fun postLogin(loginRequest: LoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postRegisterLogin(loginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    when (response.body.status.code) {
                        USER_NOT_FOUND -> {
                            snackbarMessage.value = "User Not Found"
                        }

                        USER_BANNED -> {
                            snackbarMessage.value = "User Has Banned"
                        }

                        OK -> {
                            loginResponse.value = response.body.data.let { response.body.data }
                        }
                    }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postCarerLogin(carerLoginRequest: CarerLoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postCarerLogin(carerLoginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let {
                        carerLoginSuccess.value = it
                    }

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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postKidLogin(kidLoginRequest: KidLoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postKidLogin(kidLoginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    when (response.body.status.code) {
                        OK -> {
                            kidLoginSuccess.value = response.body.data.let { response.body.data }
                        }
                    }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun getAllProfileSelection(loginToken: String) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getAllProfileSelection(loginToken)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let {
                        allProfileSelection.value = it
                    }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postKidForgotPin(request: ForgotPinKidRequest){
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidForgotPin(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let {
                        successKidForgotPin.call()
                    }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postCarerForgotPin(request: ForgotPinCarerRequest){
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.carerForgotPin(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let {
                        successCarerForgotPin.call()
                    }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

}