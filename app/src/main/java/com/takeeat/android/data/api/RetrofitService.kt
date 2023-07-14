package com.takeeat.android.data.api

import com.takeeat.android.data.model.RefrigeratorIngredientList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("/expiration")
    fun getNormalRefrigerator() : Call<RefrigeratorIngredientList>

    @POST("/expiration")
    fun getExpireRefrigerator() : Call<RefrigeratorIngredientList>
}