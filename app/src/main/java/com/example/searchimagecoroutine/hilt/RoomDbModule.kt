package com.example.searchimagecoroutine.hilt

import android.content.Context
import androidx.room.Room
import com.example.searchimagecoroutine.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomDbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "searchImage-db"
        ).build()

    @Provides
    fun provideSearchImageDao(appDatabase: AppDatabase) = appDatabase.searchImageDao()
}
