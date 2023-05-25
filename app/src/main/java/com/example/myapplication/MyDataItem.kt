package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class MyResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    val results: List<MyDataItem>
)

data class MyDataItem(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
)