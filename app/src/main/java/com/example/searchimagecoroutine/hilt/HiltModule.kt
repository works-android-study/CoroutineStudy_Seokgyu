package com.example.searchimagecoroutine.hilt

import com.example.searchimagecoroutine.SearchImageRepository
import com.example.searchimagecoroutine.api.SearchImageService
import com.example.searchimagecoroutine.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {
    @Singleton
    @Provides
    fun provideSearchImageRepository(
        searchImageService: SearchImageService,
        appDatabase: AppDatabase
    ): SearchImageRepository {
        return SearchImageRepository(searchImageService, appDatabase)
    }
}
