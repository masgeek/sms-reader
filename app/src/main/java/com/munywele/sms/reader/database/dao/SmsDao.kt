package com.munywele.sms.reader.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.munywele.sms.reader.database.entities.SmsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smsEntity: SmsEntity)

    @Query("SELECT * from sms order by date dESC")
    fun getAllSms(): Flow<List<SmsEntity>>

    @Query("DELETE FROM sms")
    suspend fun deleteAll()
}