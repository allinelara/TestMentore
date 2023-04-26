package com.allinedelara.network

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import java.nio.charset.Charset

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
            val correctUrl = this.url.toString() == fakeURl
            val correctBody = getBody(body) == "\"$fakeItem\""
            correctUrl && correctBody
        })).doReturn(mockCall)

        //when
        runBlocking {
            testedSource.put(fakeItem)
        }

        //then
       verify(mockCall).execute()
    }

    @org.junit.jupiter.api.Test
    fun testDelete(){
        //given
        val fakeURl = "https://test.com/"
        val fakeItem = "1"
        val mockCall : Call = mock()
        whenever(mockSource.getUrl()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            val correctUrl = this.url.toString() == fakeURl
            val correctBody = getBody(body) == "\"$fakeItem\""
            correctUrl && correctBody
        })).doReturn(mockCall)

        //when
        runBlocking {
            testedSource.delete(fakeItem)
        }

        //then
        verify(mockCall).execute()
    }

    @org.junit.jupiter.api.Test
    fun testPutList(){
        //given
        val fakeURl = "https://test.com/"
        val fakeItems = listOf("item1", "item2", "item3")
        val mockCall : Call = mock()
        whenever(mockSource.getUrl()).doReturn(fakeURl)
        whenever(mockHttpClient.newCall(argThat{
            val correctUrl = this.url.toString() == fakeURl
            val correctBody = getBody(body) == "\"$fakeItems\""
            correctUrl && correctBody
        })).doReturn(mockCall)

        //when
        runBlocking {
            testedSource.putList(fakeItems)
        }

        //then
        verify(mockCall).execute()
    }
}

private fun getBody(body: RequestBody?):String?{
    return if(body == null){
        null
    }else{
        try {
            val sync = Buffer()
            val charSet = Charset.defaultCharset()
            body.writeTo(sync)
            sync.readString(charSet)
        }catch (e: Exception){
            null
        }
    }
}
