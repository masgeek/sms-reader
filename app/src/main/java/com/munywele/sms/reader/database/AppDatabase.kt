package com.munywele.sms.reader.database

import androidx.room.Database
import com.munywele.sms.reader.database.dao.SmsDao
import com.munywele.sms.reader.database.entities.Sms

@Database(entities = [Sms::class], version = 1)
abstract class AppDatabase {
    abstract fun smsDao(): SmsDao
}