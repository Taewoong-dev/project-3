package com.boost.issueTracker.data.network.demo

import android.util.Log
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import kotlinx.serialization.json.Json
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoIssueDataSourceImpl @Inject constructor(
    private val assetManager: IssueAssetManager = JvmIssueUnitTest,
    private val json: Json
): DemoIssueDataSource {

    override suspend fun receiveIssues(): List<IssueResponse> {
        val inputStream: InputStream = assetManager.open("issues.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val serial =  json.decodeFromString<List<IssueResponse>>(jsonString)
        Log.e("DemoIssueDataSourceImpl", "receiveIssues: $serial")

        return serial
    }
}