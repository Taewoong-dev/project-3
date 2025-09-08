package com.boost.issueTracker.data.network.demo.di

import com.boost.issueTracker.data.network.demo.DemoIssueDataSourceImpl
import com.boost.issueTracker.data.network.demo.DemoIssueDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DemoIssueModule {

    @Binds
    fun binds(impl: DemoIssueDataSourceImpl): DemoIssueDataSource
}