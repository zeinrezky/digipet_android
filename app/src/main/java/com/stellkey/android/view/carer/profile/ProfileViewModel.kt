package com.stellkey.android.view.carer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.*
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.model.request.CreateRewardRequest
import com.stellkey.android.model.request.CustomTaskRequest
import com.stellkey.android.model.request.DeleteChildTaskRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()

    val getListRewardsSuccess = SingleLiveEvent<Unit>()
    val createAssignmentSuccess = SingleLiveEvent<Unit>()
    val deleteAssignmentSuccess = SingleLiveEvent<Unit>()
    val createRewardSuccess = SingleLiveEvent<Unit>()

    val detailCarer = MutableLiveData<AllCarersModel?>()
    val detailKid = MutableLiveData<AllKidsModel?>()
    val createNewTaskSuccess = SingleLiveEvent<CustomTaskModel?>()

    val activeTasks = MutableLiveData<AllKidsModel.Assignments?>()
    val groupedChallenges = MutableLiveData<GroupedChallengesModel?>()
    val challengeCategories = MutableLiveData<ArrayList<ChallengeCategoryModel>?>()
    val globalReward = MutableLiveData<RewardListModel?>()

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

    fun getCurrentCycleAssignment(profileId: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getCurrentCycleAssignment(profileId = profileId)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    activeTasks.value = response.body.data
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

    fun getListReward(profileId: Int, starCost: Int?) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response =
                userRepository.getListReward(profileId = profileId, starCost = starCost)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    getListRewardsSuccess.call()
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

    fun getGroupedChallenges(age: Int) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getGroupedChallenges(age = age)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    groupedChallenges.value = response.body.data
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

    fun getChallengeCategory() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getChallengeCategory()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    challengeCategories.value = response.body.data
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

                else -> {}
            }
        }
    }

    fun deleteAssignment(deleteKidTaskRequest: DeleteChildTaskRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.deleteKidAssignment(deleteKidTaskRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    deleteAssignmentSuccess.call()
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

    fun getListGlobalReward() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getListGlobalReward()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    globalReward.value = response.body.data
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

    fun postCreateReward(createRewardRequest: CreateRewardRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postCreateReward(createRewardRequest)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    createRewardSuccess.call()
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

    fun postNewTaskAssignment(request: CustomTaskRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postNewCustomChallenge(request)){
                is NetworkResponse.Success ->{
                    isLoading.value = false
                    createNewTaskSuccess.value = response.body.data
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
                is NetworkResponse.UnknownError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.error.message.toString()
                }
            }
        }
    }
}