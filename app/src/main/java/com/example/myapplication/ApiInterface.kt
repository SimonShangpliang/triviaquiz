package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {



    @GET(value = "api.php")

fun getData(@Query("amount") amount: Int,  @Query("difficulty") difficulty: String,@Query("type") type: String,@Query("category") category: Int): Call<MyResponse>

}