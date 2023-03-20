package com.allinedelara.network

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach

class RemoteDataSyncTest {

    lateinit var testedSource: RemoteDataSync<String,String>
    lateinit var mockHttpClient: OkHttpClient
    lateinit var mockSource: RemoteDataSync<String,String>

    @BeforeEach
    fun setUp(){
        mockHttpClient = mock()
        mockSource = mock()
        testedSource = object: RemoteDataSync<String,String>(mockHttpClient){
            override fun getUrl(): String {
                return mockSource.getUrl()
            }
        }
    }

    @org.junit.jupiter.api.Test
    fun testPut(){
        //given
        val fakeURl = "https://test.com/"
        val fakeItem = "item"
        val mockCall : Call = mock()
        whenever(mockSource.getUrl()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)

        //when
        val results = runBlocking {
            testedSource.put(fakeItem)
        }

        //then
        Assertions.assertThat(results).isNotNull
    }

    @org.junit.jupiter.api.Test
    fun testDelete(){
        //given
        val fakeURl = "https://test.com/"
        val fakeId = "1"
        val mockCall : Call = mock()
        whenever(mockSource.getUrl()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)

        //when
        val results = runBlocking {
            testedSource.delete(fakeId)
        }

        //then
        Assertions.assertThat(results).isNotNull
    }

    @org.junit.jupiter.api.Test
    fun testPutList(){
        //given
        val fakeURl = "https://test.com/"
        val fakeItem = arrayListOf<String>("item", "item2")
        val mockCall : Call = mock()
        whenever(mockSource.getUrl()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)

        //when
        val results = runBlocking {
            testedSource.putList(fakeItem)
        }

        //then
        Assertions.assertThat(results).isNotNull
    }

    private fun mockResponse(statusCode: Int, message: String): Response {
        val fakeResponse = Response.Builder()
            .request(Request.Builder().url("https://test.com").get().build())
            .code(statusCode)
            .message(message)
            .protocol(Protocol.HTTP_2)
            //.body(body)
            .build()
        return fakeResponse
    }
}
