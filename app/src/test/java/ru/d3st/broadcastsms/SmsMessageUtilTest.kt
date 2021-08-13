package ru.d3st.broadcastsms

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SmsMessageUtilTest {


    @Before
    fun setUp() {
    }

    @Test
    fun codeFromSMS() {
        val message = "code is 7425"
        val code = SmsMessageUtil.codeFromSMS(message)
        val expected = "7425"
        assertEquals(expected, code)
    }
}