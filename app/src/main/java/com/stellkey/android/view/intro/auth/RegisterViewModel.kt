package com.stellkey.android.view.intro.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.model.ConfirmAccountModel
import com.stellkey.android.model.KidGlobalChallengeModel
import com.stellkey.android.model.LoginModel
import com.stellkey.android.model.ProfileIconModel
import com.stellkey.android.model.request.*
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val registerSuccess = SingleLiveEvent<Unit>()
    val resendCodeSuccess = SingleLiveEvent<Unit>()
    val setupDefaultCarerSuccess = SingleLiveEvent<Unit>()
    val createKidSuccess = SingleLiveEvent<Unit>()
    val createAssignmentSuccess = SingleLiveEvent<Unit>()
    val subscriptionSuccess = SingleLiveEvent<Unit>()

    val confirmAccountResponse = MutableLiveData<ConfirmAccountModel.ConfirmAccountModelData>()
    val listProfileIcons = MutableLiveData<ArrayList<ProfileIconModel.ProfileIconModelData>>()
    val listGlobalChallenges = MutableLiveData<ArrayList<KidGlobalChallengeModel>>()
    val registerLoginResponse = MutableLiveData<LoginModel>()

    fun register(register: RegisterEmailRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.register(register)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    AppPreference.deleteAll()
                    AppPreference.putAccountId(response.body.data.accountId)
                    registerSuccess.call()
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
            }
        }
    }

    fun resendCode(resendCode: ResendPINRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.resendCode(resendCode)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    resendCodeSuccess.call()
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
            }
        }
    }

    fun confirmAccount(confirmRequest: RegisterConfirmRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.confirmAccount(confirmRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    confirmAccountResponse.value = response.body.data
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
            }
        }
    }

    fun getAllProfileIcons() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getAllProfileIcons()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listProfileIcons.value = response.body.data
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
            }
        }
    }

    fun postSetupDefaultCarer(defaultCarerRequest: DefaultCarerRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postSetupDefaultCarer(
                AppPreference.getCarerToken(),
                defaultCarerRequest
            )) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    AppPreference.deleteProfileIcon()
                    setupDefaultCarerSuccess.call()
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
            }
        }
    }

    fun postCreateKid(createChildRequest: CreateChildRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.postCreateKid(AppPreference.getCarerToken(), createChildRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    AppPreference.putTempChildId(response.body.data.id)
                    AppPreference.putTempChildAge(response.body.data.age)
                    createKidSuccess.call()
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
            }
        }
    }

    fun getGlobalChallenges(age: Int?) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.getGlobalChallenge(
                    carerToken = AppPreference.getCarerToken(),
                    age = age
                )) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listGlobalChallenges.value = response.body.data.toArrayList()
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
            }
        }
    }

    fun postCreateAssignment(createAssignmentRequest: CreateAssignmentRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postCreateAssignment(
                carerToken = AppPreference.getCarerToken(),
                createAssignmentRequest
            )) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    createAssignmentSuccess.call()
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
            }
        }
    }

    fun postSubscription(subscriptionRequest: SubscriptionRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postSubscription(subscriptionRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    subscriptionSuccess.call()
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
            }
        }
    }

    fun postRegisterLogin(loginRequest: LoginRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postRegisterLogin(loginRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    registerLoginResponse.value = response.body.data.let { response.body.data }
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
            }
        }
    }

}