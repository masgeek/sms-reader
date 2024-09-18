package com.munywele.sms.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.munywele.sms.database.entities.SmsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smsEntity: SmsEntity)

    @Query("SELECT * from sms order by timestamp DESC")
    fun getAllSms(): LiveData<List<SmsEntity>>

    @Query("SELECT * FROM sms WHERE sender=:sender AND amount >= :minAmount AND body LIKE '%' || :content || '%' ORDER BY timestamp DESC")
    fun getFilteredSms(
        sender: String,
        minAmount: Double,
        content: String
    ): LiveData<List<SmsEntity>>


    @Query("DELETE FROM sms")
    suspend fun deleteAll()
}