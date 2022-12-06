package com.stellkey.android.view.carer.family

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.model.KidGlobalChallengeModel
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.model.request.CreateCarerRequest
import com.stellkey.android.model.request.CreateChildRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class FamilyViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val createKidSuccess = SingleLiveEvent<Unit>()
    val createAssignmentSuccess = SingleLiveEvent<Unit>()
    val createCarerSuccess = SingleLiveEvent<Unit>()

    val listGlobalChallenges = MutableLiveData<ArrayList<KidGlobalChallengeModel>>()

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

    fun postCreateCarer(createCarerRequest: CreateCarerRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postCreateCarer(
                createCarerRequest
            )) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    createCarerSuccess.call()
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