package com.boost.issueTracker.data.network.demo

import com.boost.issueTracker.BuildConfig
import java.io.InputStream
import kotlin.io.path.Path
import kotlin.io.path.inputStream

object JvmIssueUnitTest: IssueAssetManager {
    override fun open(fileName: String): InputStream = try{
        Path(BuildConfig.JSON_FILE_PATH).inputStream()
    }catch (e: Exception){
        println("Error: ${e.message}")
        throw e
    }
}