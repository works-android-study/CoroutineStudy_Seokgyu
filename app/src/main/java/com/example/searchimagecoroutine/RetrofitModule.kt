package com.example.searchimagecoroutine

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    fun provideInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .apply {
                        //Todo addHeader 필요
                    }
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideSearchImageService(
        okHttpClient: OkHttpClient
    ): SearchImageService {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(SearchImageService::class.java)
    }

}
