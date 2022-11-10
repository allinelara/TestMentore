package com.allinedelara.network

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import java.io.InputStream

class RemoteDataSourceTest{

    lateinit var testedSource: RemoteDataSource<String,String>
    lateinit var mockHttpClient: OkHttpClient
    lateinit var mockSource: RemoteDataSource<String,String>


    private fun mockResponse(statusCode: Int, message: String, body: ResponseBody): Response {
        val fakeResponse = Response.Builder()
            .request(Request.Builder().url("https://test.com").get().build())
            .code(statusCode)
            .message(message)
            .protocol(Protocol.HTTP_2)
            .body(body)
            .build()
        return fakeResponse
    }

    @BeforeEach
    fun setUp(){
        mockHttpClient = mock()
        mockSource = mock()
        testedSource = object: RemoteDataSource<String,String>(mockHttpClient){
            override fun getUrl(id: String): String {
                return mockSource.getUrl(id)
            }

            override fun getUrlList(): String {
                return mockSource.getUrlList()
            }

            override fun parseSingleItem(input: InputStream): String? {
                return mockSource.parseSingleItem(input)
            }

            override fun parseList(input: InputStream): List<String>? {
                return mockSource.parseList(input)
            }

        }
    }

    @org.junit.jupiter.api.Test
    fun testGet(){
        //given
        val fakeURl = "jijio"
        val fakeId = "oi"
        val mockInputStream : InputStream = mock()
        val mockBody = mock<ResponseBody>()
        val mockResponse = mockResponse(200, "test",mockBody )
        val mockCall : Call = mock()
        val fakeItem = "item"
        whenever(mockSource.getUrl(fakeId)).doReturn(fakeURl)
        whenever(mockSource.parseSingleItem(mockInputStream)).doReturn(fakeItem)
        doReturn(mockInputStream).whenever(mockBody).byteStream()
        whenever(mockHttpClient.newCall(any())).doReturn(mockCall)
        whenever(mockCall.execute()).doReturn(mockResponse)

    //when
        val results = runBlocking {
           testedSource.get(fakeId)
        }

    //then
        assertThat(results).isEqualTo(fakeItem)
    }
}