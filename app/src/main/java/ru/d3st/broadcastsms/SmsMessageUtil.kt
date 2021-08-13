package ru.d3st.broadcastsms

import android.os.Build
import android.telephony.SmsMessage

object SmsMessageUtil {

    /**
     * Функция из строчки вынимает цифровые значения
     */

    fun codeFromSMS(message: String):String{
        return message.filter { it.isDigit() }
    }

}