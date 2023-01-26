package com.stellkey.android.view.child

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.KidInfoModel
import com.stellkey.android.model.KidLogModel
import com.stellkey.android.model.KidRewardRedemption
import com.stellkey.android.model.PetModel
import com.stellkey.android.model.PetStore
import com.stellkey.android.model.request.KidCompleteTaskRequest
import com.stellkey.android.model.request.KidRedeemRewardRequest
import com.stellkey.android.model.request.LocaleModel
import com.stellkey.android.model.request.UpdateLocaleRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.Constant
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ChildViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()
    val kidRedeemRewardSuccess = SingleLiveEvent<Unit>()

    val kidInfoResponse = MutableLiveData<KidInfoModel?>()
    val completeKidTaskResponse = MutableLiveData<Any?>()

    val kidLocaleResponse = MutableLiveData<LocaleModel>()
    val kidRewardsAvailableRedemption = MutableLiveData<List<KidRewardRedemption>>()
    val kidLogsResponse = MutableLiveData<List<KidLogModel>>()
    val kidTapThePetResponse = MutableLiveData<PetModel>()
    val kidPetStore = MutableLiveData<List<PetStore>>()

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

    fun postKidRewardRedeemption(request: KidRedeemRewardRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidRedeemReward(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    kidRedeemRewardSuccess.call()
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

    fun getKidLogs(sortBy: String = Constant.SortBy.DESC, month: Int, year: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidLogs(sortBy, month, year)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let { kidLogsResponse.value = it }
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

    fun postKidTapThePet() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidTapThePet()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let { kidTapThePetResponse.value = it }
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

    fun getKidPetstoreList() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.kidGetPetstore()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.items.let { kidPetStore.value = it }
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