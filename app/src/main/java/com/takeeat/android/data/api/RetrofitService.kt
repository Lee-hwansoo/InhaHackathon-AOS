package com.takeeat.android.data.api

import com.takeeat.android.data.model.RefrigeratorIngredientList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("user/sns/login")
    fun getNormalRefrigerator() : Call<RefrigeratorIngredientList>
}