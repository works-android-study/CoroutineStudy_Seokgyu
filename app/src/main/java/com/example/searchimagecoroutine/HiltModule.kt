package com.example.searchimagecoroutine

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
        searchImageService: SearchImageService
    ): SearchImageRepository {
        return SearchImageRepository(searchImageService)
    }
}
