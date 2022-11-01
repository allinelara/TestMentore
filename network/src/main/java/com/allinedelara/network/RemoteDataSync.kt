package com.allinedelara.network

import com.allinedelara.core.DataSync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody

abstract class RemoteDataSync <T, K>(val okHttpClient: OkHttpClient): DataSync<T,K> {
    abstract fun getUrl():String
    abstract fun getUrlList():String
    override suspend fun put(item: T) {
         withContext(Dispatchers.IO) {
             val mediaType = "application/json; charset=utf-8".toMediaType()

             val url = getUrl()
            val body: RequestBody = RequestBody.
            val request = Request.Builder().put(body).url(url).build()
            try {
                okHttpClient.newCall(request).execute()
            }catch (io: IOException){
                null
            }
        }
    }
}