package com.boost.issueTracker.data.network.retrofit.di

import com.boost.issueTracker.data.network.retrofit.GitIssueApi
import com.boost.issueTracker.data.network.retrofit.IssueNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {

    // IssueNetworkDataSource 인터페이스를 구현하는 GitIssueApi 객체 주입함수
    @Binds
    fun bindIssueNetworkDataSource(
        impl: GitIssueApi
    ): IssueNetworkDataSource
}