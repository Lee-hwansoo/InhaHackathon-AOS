package com.takeeat.android.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = ""
    var service: RetrofitService

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


    internal class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()

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