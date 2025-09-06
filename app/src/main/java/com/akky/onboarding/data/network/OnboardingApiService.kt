package com.akky.onboarding.data.network

import com.akky.onboarding.data.model.OnboardingResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface OnboardingApiService {
    @GET("_assets/shared/education-metadata.json")
    suspend fun getEducationMetadata(): OnboardingResponse
    
    companion object {
        fun create(): OnboardingApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            
            val retrofit = Retrofit.Builder()
                .baseUrl("https://myjar.app/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            return retrofit.create(OnboardingApiService::class.java)
        }
    }
}
