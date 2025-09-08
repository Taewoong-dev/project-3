package com.boost.issueTracker.data.repository.di

import com.boost.issueTracker.data.repository.DataRepository
import com.boost.issueTracker.data.repository.NetworkIssueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsIssueRepository(
        issueRepository: NetworkIssueRepositoryImpl,
    ): DataRepository
}