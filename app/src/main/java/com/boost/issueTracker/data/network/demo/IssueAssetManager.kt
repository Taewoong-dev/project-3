package com.boost.issueTracker.data.network.demo

import java.io.InputStream

fun interface IssueAssetManager {
    fun open(fileName: String): InputStream
}