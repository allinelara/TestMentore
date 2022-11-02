package com.allinedelara.network

import com.allinedelara.core.DataSync
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


abstract class RemoteDataSync <T, K>(val okHttpClient: OkHttpClient): DataSync<T,K> {
    abstract fun getUrl():String
    override suspend fun put(item: T) {
         withContext(Dispatchers.IO) {
             val url = getUrl()
             val jsonItem: String = Gson().toJson(item)
             val mediaType = "application/json; charset=utf-8".toMediaType()

             val requestBody = jsonItem.toRequestBody(mediaType)

             val request: Request = Request.Builder()
                 .url(url)
                 .put(requestBody)
                 .build()
             try {
                 okHttpClient.newCall(request).execute()
             }catch (io: IOException){
                 null
             }
        }
    }

    override suspend fun putList(listItems: List<T>) {
        withContext(Dispatchers.IO) {
            val url = getUrl()
            val jsonItem: String = Gson().toJson(listItems)
            val mediaType = "application/json; charset=utf-8".toMediaType()

            val requestBody = jsonItem.toRequestBody(mediaType)

            val request: Request = Request.Builder()
                .url(url)
                .put(requestBody)
                .build()
            try {
                okHttpClient.newCall(request).execute()
            }catch (io: IOException){
                null
            }
        }
    }

    override suspend fun delete(id: K) {
        withContext(Dispatchers.IO) {
            val url = getUrl()
            val jsonItem: String = Gson().toJson(id)
            val mediaType = "application/json; charset=utf-8".toMediaType()

            val requestBody = jsonItem.toRequestBody(mediaType)

            val request: Request = Request.Builder()
                .url(url).delete(requestBody)
                .build()
            try {
                okHttpClient.newCall(request).execute()
            }catch (io: IOException){
                null
            }
        }
    }
}