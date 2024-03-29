package com.stellkey.android.view.carer.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.model.AllCarersModel
import com.stellkey.android.model.AllKidsModel
import com.stellkey.android.model.ChallengeCategoryModel
import com.stellkey.android.model.CustomTaskModel
import com.stellkey.android.model.GroupedChallengesModel
import com.stellkey.android.model.RewardListModel
import com.stellkey.android.model.RewardModel
import com.stellkey.android.model.request.CreateAssignmentRequest
import com.stellkey.android.model.request.CreateRewardRequest
import com.stellkey.android.model.request.CustomTaskRequest
import com.stellkey.android.model.request.DeleteChildTaskRequest
import com.stellkey.android.model.request.CustomRewardAssignKidRequest
import com.stellkey.android.model.request.GlobalRewardAssignKidRequest
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.AppPreference
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()

    val createAssignmentSuccess = SingleLiveEvent<Unit>()
    val deleteAssignmentSuccess = SingleLiveEvent<Unit>()
    val createRewardSuccess = SingleLiveEvent<Unit>()
    val assignRewardForKidsSuccess = SingleLiveEvent<Unit>()
    val assignRewardForKidsFailed = SingleLiveEvent<Unit>()
    val unAssignGlobalRewardForKidSuccess = SingleLiveEvent<Unit>()
    val unAssignCustomRewardForKidSuccess = SingleLiveEvent<Unit>()

    val detailCarer = MutableLiveData<AllCarersModel?>()
    val detailKid = MutableLiveData<AllKidsModel?>()
    val createNewTaskSuccess = SingleLiveEvent<CustomTaskModel?>()

    val activeTasks = MutableLiveData<AllKidsModel.Assignments?>()
    val groupedChallenges = MutableLiveData<GroupedChallengesModel?>()
    val challengeCategories = MutableLiveData<ArrayList<ChallengeCategoryModel>?>()
    val globalReward = MutableLiveData<RewardListModel?>()
    val listKids = MutableLiveData<ArrayList<AllKidsModel>?>()
    val getListRewardsSuccess = MutableLiveData<List<RewardModel>?>()

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

                else -> {
                    isLoading.value = false
                }
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

                else -> {
                    isLoading.value = false
                }
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

                else -> {
                    isLoading.value = false
                }
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
                    getListRewardsSuccess.value = response.body.data
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

                else -> {
                    isLoading.value = false
                }
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

                else -> {
                    isLoading.value = false
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

                else -> {
                    isLoading.value = false
                }
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun getListGlobalReward() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getListGlobalReward()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    globalReward.postValue(response.body.data)
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

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postAssignCustomRewardForKids(request : CustomRewardAssignKidRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.assignCustomRewardForKids(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    assignRewardForKidsSuccess.call()
                }

                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    assignRewardForKidsFailed.call()
                }

                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                    assignRewardForKidsFailed.call()
                }

                else -> {
                    isLoading.value = false
                }
            }
        }
    }

    fun postAssignGlobalRewardForKids(request : GlobalRewardAssignKidRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.assignGlobalRewardForKids(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    assignRewardForKidsSuccess.call()
                }

                is NetworkResponse.ServerError -> {
                    isLoading.value = false
                    snackbarMessage.value = response.body?.message
                    assignRewardForKidsFailed.call()
                }

                is NetworkResponse.NetworkError -> {
                    isLoading.value = false
                    networkError.value = response.error.message.toString()
                    snackbarMessage.value = response.error.message.toString()
                    assignRewardForKidsFailed.call()
                }

                else -> {
                    isLoading.value = false
                }
            }
        }
    }


    fun postUnAssignGlobalRewardForKids(request : GlobalRewardAssignKidRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.unassignGlobalRewardForKids(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    unAssignGlobalRewardForKidSuccess.call()
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

    fun postUnAssignCustomRewardForKids(request : CustomRewardAssignKidRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.unassignCustomRewardForKids(request)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    unAssignCustomRewardForKidSuccess.call()
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

    fun postNewTaskAssignment(request: CustomTaskRequest) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.postNewCustomChallenge(request)) {
                is NetworkResponse.Success -> {
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

    fun getListAllKids() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getListAllKids()) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listKids.value = response.body.data
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