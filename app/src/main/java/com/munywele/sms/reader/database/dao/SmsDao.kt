package com.munywele.sms.reader.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.munywele.sms.reader.database.entities.SmsEntity

@Dao
interface SmsDao {
    @Insert
    suspend fun insert(smsEntity: SmsEntity)

    @Query("SELECT * from sms order by date dESC")
    fun getAllSms(): List<SmsEntity>

    @Query("DELETE FROM sms")
    suspend fun deleteAll()
}