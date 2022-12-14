package com.example.searchimagecoroutine.fake

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.searchimagecoroutine.SearchImageRepository
import com.example.searchimagecoroutine.api.SearchImageService
import com.example.searchimagecoroutine.hilt.HiltModule
import com.example.searchimagecoroutine.hilt.RetrofitModule
import com.example.searchimagecoroutine.hilt.RoomDbModule
import com.example.searchimagecoroutine.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltModule::class, RetrofitModule::class, RoomDbModule::class]
)
class FakeHiltModule {
    @Singleton
    @Provides
    fun provideSearchImageRepository(
        searchImageService: SearchImageService,
        appDatabase: AppDatabase
    ): SearchImageRepository {
        return Mockito.mock(SearchImageRepository::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()

    @Provides
    fun provideSearchImageDao(appDatabase: AppDatabase) = appDatabase.searchImageDao()

    @Provides
    fun provideContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }

    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    fun provideInterceptorOkHttpClient(): OkHttpClient {
        return Mockito.mock(OkHttpClient::class.java)
    }

    @Provides
    fun provideSearchImageService(
        okHttpClient: OkHttpClient
    ): SearchImageService {
        return Mockito.mock(SearchImageService::class.java)
    }
}
