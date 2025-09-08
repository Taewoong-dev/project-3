package com.boost.issueTracker.data.network.demo.di

import android.content.Context
import com.boost.issueTracker.data.network.demo.IssueAssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DemoProvideModule {
    @Provides
    @Singleton
    fun providesAssetManager(
        @ApplicationContext context: Context,
    ): IssueAssetManager = IssueAssetManager(context.assets::open)

    @Provides
    fun providesJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
}