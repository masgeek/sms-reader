package com.munywele.sms.reader.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.munywele.sms.reader.database.dao.SmsDao
import com.munywele.sms.reader.database.entities.SmsEntity

@Database(entities = [SmsEntity::class], version = 1)
abstract class SmsDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao


    companion object {
        @Volatile
        private var INSTANCE: SmsDatabase? = null

        fun getDatabase(context: Context): SmsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmsDatabase::class.java,
                    "sms_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}