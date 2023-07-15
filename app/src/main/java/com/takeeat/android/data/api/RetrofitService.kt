package com.takeeat.android.data.api

import com.google.gson.JsonObject
import com.takeeat.android.data.model.RefrigeratorIngredientList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitService {
    @POST("/expiration")
    fun getNormalRefrigerator() : Call<RefrigeratorIngredientList>

    @POST("/expiration")
    fun getExpireRefrigerator() : Call<RefrigeratorIngredientList>

    @POST("/api/v1/refrigerator/ingredient")
    @Multipart
    fun registReciept(@Part("content") content: RequestBody, @Part image: MultipartBody.Part): Call<JsonObject>
}