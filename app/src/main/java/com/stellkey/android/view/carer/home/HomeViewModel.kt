package com.stellkey.android.view.carer.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.request.AssignmentActionRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()
    val getTodayAssignmentSuccess = SingleLiveEvent<Unit>()
    val deleteKidSuccess = SingleLiveEvent<Unit>()
    val deleteCarerSuccess = SingleLiveEvent<Unit>()
    val confirmKidTaskCompletionSuccess = SingleLiveEvent<Unit>()
    val confirmKidTaskWithoutCompletionSuccess = SingleLiveEvent<Unit>()
    val declineKidTaskCompletionSuccess = SingleLiveEvent<Unit>()
    val declineKidTaskWithoutCompletionSuccess = SingleLiveEvent<Unit>()

    val listAllKids = MutableLiveData<ArrayList<AllKidsModel>>()
    val listAllCarers = MutableLiveData<ArrayList<AllCarersModel>>()
    val todayAssignment = MutableLiveData<AllKidsModel.Assignments?>()
    val yesterdayAssignment = MutableLiveData<AllKidsModel.Assignments?>()
    val currentCycleAssignmentSuccess = MutableLiveData<AllKidsModel.Assignments?>()

    fun getCurrentCycleAssignment(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getCurrentCycleAssignment(profileId = profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    response.body.data.let {
                        currentCycleAssignmentSuccess.value = it
                    }

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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun getTodayAssignment(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getTodayAssignment(profileId = profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    todayAssignment.value = response.body.data
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun getYesterdayAssignment(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getYesterdayAssignment(profileId = profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    yesterdayAssignment.value = response.body.data
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

                is NetworkResponse.UnknownError -> {}
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

                is NetworkResponse.UnknownError -> {
                    isLoading.value = false
                }
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

                is NetworkResponse.UnknownError -> {}
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

                is NetworkResponse.UnknownError -> {}
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun postConfirmKidTaskCompletion(assignmentRequest: AssignmentActionRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.postConfirmAssignmentCompletion(assignmentRequest = assignmentRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    confirmKidTaskCompletionSuccess.call()
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun postConfirmKidTaskWithoutCompletion(assignmentRequest: AssignmentActionRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.postConfirmAssignmentWithoutCompletion(assignmentRequest = assignmentRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    confirmKidTaskWithoutCompletionSuccess.call()
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun postDeclineKidTaskCompletion(assignmentRequest: AssignmentActionRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.postDeclineAssignmentCompletion(assignmentRequest = assignmentRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    declineKidTaskCompletionSuccess.call()
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

    fun postDeclineKidTaskWithoutCompletion(assignmentRequest: AssignmentActionRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.postDeclineAssignmentWithoutCompletion(assignmentRequest = assignmentRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    declineKidTaskWithoutCompletionSuccess.call()
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

                is NetworkResponse.UnknownError -> {}
            }
        }
    }

}