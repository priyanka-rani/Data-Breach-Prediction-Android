package com.pri.databreachprediction

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.pri.databreachprediction.api.ApiService
import com.pri.databreachprediction.api.RequestModel
import com.pri.databreachprediction.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val apiService: ApiService) : ViewModel() {
    val text = MutableLiveData<String>()
    val breachType = MutableLiveData<String>()
    private val _breach = MutableLiveData<String>()
    val prediction = _breach.switchMap {
        val result = MutableLiveData<Resource<List<String>?>>(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val apiResult = try {
                Resource.success(apiService.getPrediction(RequestModel(it.split("|"))))
            } catch (e: Exception) {
                Resource.error(e.message.toString(), null)
            }
            result.postValue(apiResult)
        }
        result
    }

    fun callPredictionApi(dataBreach: String) {
        _breach.value = dataBreach

    }
}