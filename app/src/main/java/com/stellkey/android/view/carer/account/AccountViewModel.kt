package com.stellkey.android.view.carer.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.SubscriptionModel
import com.stellkey.android.model.request.*
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class AccountViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()

    val deleteLogSuccess = SingleLiveEvent<Unit>()
    val updateMainCarerPasswordSuccess = SingleLiveEvent<Unit>()
    val updateCarerSuccess = SingleLiveEvent<Unit>()
    val updateCarerPINSuccess = SingleLiveEvent<Unit>()
    val deleteCarerSuccess = SingleLiveEvent<Unit>()
    val updateKidSuccess = SingleLiveEvent<Unit>()
    val updateKidPINSuccess = SingleLiveEvent<Unit>()
    val deleteKidSuccess = SingleLiveEvent<Unit>()

    val subscriptionResponse = MutableLiveData<ArrayList<SubscriptionModel>>()
    val listAllKids = MutableLiveData<ArrayList<AllKidsModel>>()
    val listAllCarers = MutableLiveData<ArrayList<AllCarersModel>>()
    val detailCarer = MutableLiveData<AllCarersModel?>()
    val detailKid = MutableLiveData<AllKidsModel?>()

    val carerLocaleResponse = MutableLiveData<LocaleModel>()

    fun getSubscription() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getSubscription()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    subscriptionResponse.value = response.body.data.toArrayList()
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

    fun deleteLog(deleteLogRequest: DeleteLogRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.deleteLog(deleteLogRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    deleteLogSuccess.call()
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

    fun getListAllKids() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getListAllKids()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listAllKids.value = response.body.data.toArrayList()
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
                else -> {}
            }
        }
    }

    fun getListAllCarers() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getListAllCarers()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listAllCarers.value = response.body.data.toArrayList()
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
                else -> {}
            }
        }
    }

    fun getDetailCarer(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getDetailCarer(profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    detailCarer.value = response.body.data
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
                else -> {}
            }
        }
    }

    fun putUpdateCarer(profileId: Int, updateCarerRequest: EditCarerRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.editCarer(profileId, updateCarerRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    updateCarerSuccess.call()
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
                else -> {}
            }
        }
    }

    fun putUpdateCarerPIN(profileId: Int, editProfilePINRequest: EditProfilePINRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.editCarerPIN(profileId, editProfilePINRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    updateCarerPINSuccess.call()
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
                else -> {}
            }
        }
    }

    fun deleteCarer(carerId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.deleteCarer(carerId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    deleteCarerSuccess.call()
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

    fun getDetailKid(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getDetailKid(profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    detailKid.value = response.body.data
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
                else -> {}
            }
        }
    }

    fun putUpdateKid(profileId: Int, updateKidRequest: EditKidRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.editKid(profileId, updateKidRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    updateKidSuccess.call()
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
                else -> {}
            }
        }
    }

    fun putUpdateKidPIN(profileId: Int, editProfilePINRequest: EditProfilePINRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.editKidPIN(profileId, editProfilePINRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    updateKidPINSuccess.call()
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
                else -> {}
            }
        }
    }

    fun deleteKid(kidId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.deleteKid(kidId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    deleteKidSuccess.call()
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

    fun putUpdateMainCarerPassword(updatePasswordRequest: EditPasswordRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.editMainCarerPassword(updatePasswordRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    updateMainCarerPasswordSuccess.call()
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
                else -> {}
            }
        }
    }

    fun putCarerLocale(localeRequest: UpdateLocaleRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.putCarerLocale(localeRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    carerLocaleResponse.value = response.body.data.let { response.body.data }
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