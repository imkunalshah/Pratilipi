package com.kunal.pratilipi.di

import android.content.Context
import com.kunal.pratilipi.AppDatabase
import com.kunal.pratilipi.ContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideContentRepository(
        appDatabase: AppDatabase
    ): ContentRepository {
        return ContentRepository(appDatabase)
    }

}