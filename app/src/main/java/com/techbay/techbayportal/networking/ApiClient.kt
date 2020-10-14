package com.techbay.techbayportal.networking

import com.techbay.techbayportal.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

interface ApiListener<T> {
    fun onSuccess(body: T?)
    fun onFailure(error: Throwable)
    fun onCancel() {
        Timber.d("canceled")
    }
}

object ApiClient {

    const val BASE_URL = "http://watya.techbayportal.com/"

    private val httpClient = OkHttpClient.Builder()
    private val builder = getRetrofitBuilder()
    private val loggingInterceptor = getLoggingInterceptor()
    val retrofitt = getRetrofit()
    val client = getRetrofitClient()

    private fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun getRetrofit(): Retrofit {
        if (BuildConfig.DEBUG) {
            if (httpClient.interceptors().contains(loggingInterceptor).not()) {
                httpClient.addInterceptor(loggingInterceptor)
            }
        }
        return builder.client(httpClient.build()).build()
    }

    private fun getRetrofitClient(): Endpoint {
        return retrofitt.create(Endpoint::class.java)
    }

    fun <T> executeApi(call: Call<T>, apiListener: ApiListener<T>) {

        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                apiListener.onFailure(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (call.isCanceled) {
                    apiListener.onCancel()
                }
                if (response.isSuccessful) {
                    apiListener.onSuccess(response.body())
                } else {
                    apiListener.onFailure(Throwable(response.message()))
                }
            }
        })
    }
}