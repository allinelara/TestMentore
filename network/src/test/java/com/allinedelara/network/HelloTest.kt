package com.allinedelara.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HelloTest {

    @Test
    fun helloTest(){
        assertThat("foo").isEqualTo("bar")
    }
}