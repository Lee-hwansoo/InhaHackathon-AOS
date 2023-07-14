package com.takeeat.android.data.model

import java.io.Serializable

data class RefrigeratorIngredientList(
    val data: ArrayList<RefrigeratorIngredientData>
): Serializable

data class RefrigeratorIngredientData(
    val id: Int,
    val name: String
) : Serializable