package com.stellkey.android.view.carer.log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haroldadmin.cnradapter.NetworkResponse
import com.stellkey.android.helper.UtilityHelper.Companion.toArrayList
import com.stellkey.android.model.CarerLogModel
import com.stellkey.android.repository.UserRepository
import com.stellkey.android.util.SingleLiveEvent
import com.stellkey.android.view.base.BaseViewModel
import kotlinx.coroutines.launch

class LogViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val responseError = SingleLiveEvent<Unit>()
    val listLog = MutableLiveData<ArrayList<CarerLogModel>>()

    fun getCarerLog(type: String?) {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = userRepository.getCarerLog(type = type)) {
                is NetworkResponse.Success -> {
                    isLoading.value = false
                    listLog.value = response.body.data.toArrayList()
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
}