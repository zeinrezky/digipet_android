package com.stellkey.android.view.child

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.KidRewardRedemption
import com.stellkey.android.model.request.KidCompleteTaskRequest
import com.stellkey.android.model.request.LocaleModel
import com.stellkey.android.model.request.UpdateLocaleRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ChildViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()

    val kidInfoResponse = MutableLiveData<KidInfoModel?>()
    val completeKidTaskResponse = MutableLiveData<Any?>()

    val kidLocaleResponse = MutableLiveData<LocaleModel>()
    val kidRewardsAvailableRedemption = MutableLiveData<List<KidRewardRedemption>>()

    fun getKidInfo() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getKidInfo()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    kidInfoResponse.value = response.body.data
                }

                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    responseError.call()
                }

                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                    responseError.call()
                }

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postCompleteKidTask(kidCompleteTaskRequest: KidCompleteTaskRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidCompleteTask(kidCompleteTaskRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    completeKidTaskResponse.value = response.body.data
                }

                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    responseError.call()
                }

                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                    responseError.call()
                }

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun putKidLocale(localeRequest: UpdateLocaleRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.putKidLocale(localeRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    kidLocaleResponse.value = response.body.data.let { response.body.data }
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

    fun getKidListRewardsAvailableForRedemption() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidListRewardRedemption()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    kidRewardsAvailableRedemption.value =
                        response.body.data.let { response.body.data }
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