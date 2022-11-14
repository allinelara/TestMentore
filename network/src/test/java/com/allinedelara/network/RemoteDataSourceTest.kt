package com.allinedelara.network

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.BufferedSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import java.io.IOException
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
        val fakeURl = "https://test.com/"
        val fakeId = "oi"
        val mockInputStream : InputStream = mock()
        val mockBody = mock<ResponseBody>()
        val mockBuffer: BufferedSource = mock()
        val mockResponse = mockResponse(200, "test",mockBody )
        val mockCall : Call = mock()
        val fakeItem = "item"
        whenever(mockSource.getUrl(fakeId)).doReturn(fakeURl)
        whenever(mockSource.parseSingleItem(mockInputStream)).doReturn(fakeItem)
        whenever(mockBody.source()).doReturn(mockBuffer)
        whenever(mockBuffer.inputStream()).doReturn(mockInputStream)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doReturn(mockResponse)

    //when
        val results = runBlocking {
           testedSource.get(fakeId)
        }

    //then
        assertThat(results).isEqualTo(fakeItem)
    }

    @org.junit.jupiter.api.Test
    fun testGetNetworkFail(){
        //given
        val fakeURl = "https://test.com/"
        val fakeId = "oi"
        val mockCall : Call = mock()
        whenever(mockSource.getUrl(fakeId)).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doThrow(IOException())

        //when
        val results = runBlocking {
            testedSource.get(fakeId)
        }

        //then
        assertThat(results).isNull()
    }

    //write a test that be returns 400
    @org.junit.jupiter.api.Test
    fun testGetReturning400(){
        //given
        val fakeURl = "https://test.com/"
        val fakeId = "oi"
        val mockInputStream : InputStream = mock()
        val mockBody = mock<ResponseBody>()
        val mockBuffer: BufferedSource = mock()
        val mockResponse = mockResponse(400, "test",mockBody )
        val mockCall : Call = mock()
        val fakeItem = "item"
        whenever(mockSource.getUrl(fakeId)).doReturn(fakeURl)
        whenever(mockSource.parseSingleItem(mockInputStream)).doReturn(fakeItem)
        whenever(mockBody.source()).doReturn(mockBuffer)
        whenever(mockBuffer.inputStream()).doReturn(mockInputStream)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doReturn(mockResponse)
        whenever(mockCall.execute().code == 400).doThrow(IOException())


        //when
        val results = runBlocking {
            testedSource.get(fakeId)
        }

        //then
        assertThat(results).isNull()
    }

    //write a test what happen with the parsing fail
//    @org.junit.jupiter.api.Test
//    fun testGetFailingParse(){
//        //given
//        val mockInputStream : InputStream = mock()
//        val mockBuffer: BufferedSource = mock()
//        whenever(mockBuffer.inputStream()).(mockInputStream)
//        whenever(mockInputStream).doThrow(IOException())
//
//        //when
//        val results = runBlocking {
//            testedSource.parseList(mockInputStream)
//        }
//
//        //then
//        assertThat(results).isNull()
//    }

    @org.junit.jupiter.api.Test
    fun testGetAll(){
        //given
        val fakeURl = "https://test.com/"
        val mockInputStream : InputStream = mock()
        val mockBody = mock<ResponseBody>()
        val mockBuffer: BufferedSource = mock()
        val mockResponse = mockResponse(200, "test",mockBody )
        val mockCall : Call = mock()
        val fakeItens = listOf("item1, item2, item3")
        whenever(mockSource.getUrlList()).doReturn(fakeURl)
        whenever(mockSource.parseList(mockInputStream)).doReturn(fakeItens)
        whenever(mockBody.source()).doReturn(mockBuffer)
        whenever(mockBuffer.inputStream()).doReturn(mockInputStream)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doReturn(mockResponse)

        //when
        val results = runBlocking {
            testedSource.getAll()
        }

        //then
        assertThat(results).isEqualTo(fakeItens)
    }

    @org.junit.jupiter.api.Test
    fun testGetAllNetworkFail(){
        //given
        val fakeURl = "https://test.com/"
        val mockCall : Call = mock()
        whenever(mockSource.getUrlList()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doThrow(IOException())

        //when
        val results = runBlocking {
            testedSource.getAll()
        }

        //then
        assertThat(results).isEqualTo(emptyList<String>())
    }

    //write a test that be returns 400
    @org.junit.jupiter.api.Test
    fun testGetAllReturning400(){
        //given
        val fakeURl = "https://test.com/"
        val mockInputStream : InputStream = mock()
        val mockBody = mock<ResponseBody>()
        val mockBuffer: BufferedSource = mock()
        val mockResponse = mockResponse(400, "test",mockBody )
        val mockCall : Call = mock()
        val fakeItens = listOf("item1, item2, item3")
        whenever(mockSource.getUrlList()).doReturn(fakeURl)
        whenever(mockSource.parseList(mockInputStream)).doReturn(fakeItens)
        whenever(mockBody.source()).doReturn(mockBuffer)
        whenever(mockBuffer.inputStream()).doReturn(mockInputStream)
        whenever(mockHttpClient.newCall(argThat{
            this.url.toString() == fakeURl
        })).doReturn(mockCall)
        whenever(mockCall.execute()).doReturn(mockResponse)
        whenever(mockCall.execute().code == 400).doThrow(IOException())


        //when
        val results = runBlocking {
            testedSource.getAll()
        }

        //then
        assertThat(results).isEqualTo(emptyList<String>())
    }

}