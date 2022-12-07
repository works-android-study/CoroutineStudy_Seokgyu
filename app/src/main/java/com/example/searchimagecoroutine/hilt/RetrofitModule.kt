package com.example.searchimagecoroutine.hilt

import com.example.searchimagecoroutine.api.DownloadImageService
import com.example.searchimagecoroutine.api.ProgressResponseBody
import com.example.searchimagecoroutine.api.SearchImageService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @RetrofitQualifier.SearchRetrofit
    fun provideInterceptorOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .apply {
                        //Todo addHeader 필요
                        addHeader("X-Naver-Client-Id","LcTGnsYTrp5nQo_oVYSZ")
                        addHeader("X-Naver-Client-Secret","hMTVmf12Kk")
                    }
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideSearchImageService(
        @RetrofitQualifier.SearchRetrofit okHttpClient: OkHttpClient
    ): SearchImageService {
        return Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(SearchImageService::class.java)
    }

    companion object {
        fun createDownloadImageClient(onAttachmentDownloadUpdate: (Int) -> Unit): DownloadImageService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalResponse = chain.proceed(chain.request())
                    originalResponse.newBuilder()
                        .body(originalResponse.body()?.let { ProgressResponseBody(it,onAttachmentDownloadUpdate) })
                        .build()
                }.build()

            return Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(DownloadImageService::class.java)
        }
    }

    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO
}
