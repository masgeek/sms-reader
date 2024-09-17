package com.munywele.sms.reader.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.munywele.sms.reader.database.entities.Sms

@Dao
interface SmsDao {
    @Insert
    suspend fun insert(sms: Sms)

    @Query("SELECT * from sms")
    fun getAllSms():LiveData<List<Sms>>
}