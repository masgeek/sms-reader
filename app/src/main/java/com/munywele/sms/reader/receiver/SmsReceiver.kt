package com.munywele.sms.reader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

class SmsReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION==intent.action){
            val pdus = intent.extras?.get("pdus") as Array<*>
            val messages = pdus.map { pdu ->
                SmsMessage.createFromPdu(pdu as ByteArray)
            }
        }
    }
}