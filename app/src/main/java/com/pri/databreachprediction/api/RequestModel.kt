package com.pri.databreachprediction.api

import com.google.gson.annotations.SerializedName

data class RequestModel(
        @SerializedName("text") val text: List<String>
)