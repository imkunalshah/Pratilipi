package com.kunal.pratilipi.di

import com.kunal.pratilipi.data.db.AppDatabase
import com.kunal.pratilipi.data.repository.ContentRepository
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