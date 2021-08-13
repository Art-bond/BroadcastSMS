package ru.d3st.broadcastsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony


class SmsBroadcastReceiver: BroadcastReceiver() {


    private var smsListener: SmsListener? = null

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            // можно посмотреть номер телефона с которого отправлена смс
            var smsSender = ""
            //текст сообщения
            var smsBody = ""
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.displayOriginatingAddress
                smsBody += smsMessage.messageBody
            }
            val code = SmsMessageUtil.codeFromSMS(smsBody)
            smsListener?.onTextReceived(code)

        }
    }

    fun setListener(smsListener: SmsListener?) {
        this.smsListener = smsListener
    }

    interface SmsListener {
        fun onTextReceived(code: String)
    }
}