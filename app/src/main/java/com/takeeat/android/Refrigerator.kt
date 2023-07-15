package com.takeeat.android

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.takeeat.android.adapter.AdapterRefrigeratorExpire
import com.takeeat.android.adapter.AdapterRefrigeratorNormal
import com.takeeat.android.data.api.RetrofitClient
import com.takeeat.android.data.model.RefrigeratorIngredientData
import com.takeeat.android.data.model.restemp
import com.takeeat.android.util.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

object Refrigerator {
    private lateinit var Expire: ArrayList<RefrigeratorIngredientData>
    private lateinit var Normal: ArrayList<RefrigeratorIngredientData>
    private lateinit var adapterNormal: AdapterRefrigeratorNormal
    private lateinit var adapterExpire: AdapterRefrigeratorExpire

    fun setNormalAdapter(adapter: AdapterRefrigeratorNormal){
        this.adapterNormal = adapter
    }

    fun setExpireAdapter(adapter: AdapterRefrigeratorExpire){
        this.adapterExpire = adapter
    }

    fun getNormalAdapter(): AdapterRefrigeratorNormal{
        return this.adapterNormal
    }

    fun getExpireAdapter(): AdapterRefrigeratorExpire{
        return this.adapterExpire
    }

    suspend fun getNormalData(context: Context): ArrayList<RefrigeratorIngredientData> = withContext(
        Dispatchers.Default){
        val listData: ArrayList<RefrigeratorIngredientData> = ArrayList()

        try {
            // Retrofit의 비동기 호출을 suspend 함수로 래핑하여 사용합니다.
            val response = RetrofitClient.service.getNormalRefrigerator().execute()

            if (response.isSuccessful.not()) {
                Log.d("Refrigerator: 불러오기 실패", response.toString())
                Toast.makeText(context, "서버와 연결을 시도했으나 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            Log.d("chat: 채팅 리스트 불러오기 성공", "\n${it.toString()}")
                            listData.addAll(it.data)
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            Log.d("chat: 채팅 리스트 불러오기 실패", "[Fail]${t.toString()}")
        }

        return@withContext listData
    }

    suspend fun getExpireData(context: Context): ArrayList<RefrigeratorIngredientData> = withContext(
        Dispatchers.Default){
        val listData: ArrayList<RefrigeratorIngredientData> = ArrayList()

        try {
            // Retrofit의 비동기 호출을 suspend 함수로 래핑하여 사용합니다.
            val response = RetrofitClient.service.getExpireRefrigerator().execute()

            if (response.isSuccessful.not()) {
                Log.d("Refrigerator: 불러오기 실패", response.toString())
                Toast.makeText(context, "서버와 연결을 시도했으나 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            Log.d("chat: 채팅 리스트 불러오기 성공", "\n${it.toString()}")
                            listData.addAll(it.data)
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            Log.d("chat: 채팅 리스트 불러오기 실패", "[Fail]${t.toString()}")
        }

        return@withContext listData
    }

    suspend fun registReciept(context: Context, img: MultipartBody.Part): ArrayList<RefrigeratorIngredientData> = withContext(
        Dispatchers.Default){
        val listData: ArrayList<RefrigeratorIngredientData> = ArrayList()

        try {
            // Retrofit의 비동기 호출을 suspend 함수로 래핑하여 사용합니다.
            val jsonObject = Gson().toJson(restemp(UserSharedPreferences.getFCMToken()))
            var requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject)

            val response = RetrofitClient.service.registReciept(requestBody,img).execute()

            if (response.isSuccessful.not()) {
                Log.d("Reciept: 인식 실패", response.toString())
                Toast.makeText(context, "서버와 연결을 시도했으나 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            Log.d("Reciept: 인식 성공", "\n${it.toString()}")
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            Log.d("Reciept: 인식 실패", "[Fail]${t.toString()}")
        }

        return@withContext listData
    }

}