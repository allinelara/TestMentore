package com.allinedelara.network

import com.allinedelara.core.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.InputStream

abstract class RemoteDataSource <T,K> (val okHttpClient: OkHttpClient):DataSource<T,K>{
    abstract fun getUrl(id: K):String
    abstract fun getUrlList():String
    override suspend fun get(id: K): T? {
        return withContext(Dispatchers.IO) {
            val url = getUrl(id)
            val request = Request.Builder().get().url(url).build()
            val response = try {
                 okHttpClient.newCall(request).execute()
            }catch (io: IOException){
                null
            }
            handleResponse(response) { parseSingleItem(it) }
        }
    }

    override suspend fun getAll(): List<T> {
        return withContext(Dispatchers.IO) {
            val url = getUrlList()
            val request = Request.Builder().get().url(url).build()
            val response = try {
                okHttpClient.newCall(request).execute()
             }catch (io: IOException){
                null
            }
            handleResponse(response) { parseList(it) }.orEmpty()
        }
    }

    private fun <O> handleResponse(response: Response?, parseResponse: (InputStream) -> O?): O? {
        return try {
            if (response?.code == 200) {
                val body = response.body
                if (body != null) {
                    parseResponse(body.byteStream())
                } else {
                    null
                }
            } else {
                null
            }
        }catch(e: Exception){
            null
        }
    }

    abstract fun parseSingleItem(input:InputStream) :T?
    abstract fun parseList(input:InputStream) :List<T>?
}