package com.takeeat.android.data.api

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.takeeat.android.util.UserSharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("StaticFieldLeak")
object RetrofitClient {
    private const val BASE_URL = "http://ec2-43-200-131-2.ap-northeast-2.compute.amazonaws.com"
    var service: RetrofitService
    private lateinit var context: Context

    init {
        val interceptorClient = OkHttpClient().newBuilder()
            .addInterceptor(RequestInterceptor())
            .addInterceptor(ResponseInterceptor())
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(interceptorClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    fun initialize(context: Context) {
        this.context = context
    }


    internal class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            if (UserSharedPreferences.getFCMToken().isNotEmpty()){
                val auth = UserSharedPreferences.getFCMToken()
                builder.header("Authorization", auth)
                Log.d("인증", auth)
            }

            return chain.proceed(builder.build())
        }
    }

    internal class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)

            when (response.code()) {
                400 -> {
                    // todo Control Error
                }
                401 -> {
                    // todo Control Error
                }
                402 -> {
                    // todo Control Error
                }
            }
            return response
        }
    }
}