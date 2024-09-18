package com.munywele.sms.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.munywele.sms.database.entities.SmsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smsEntity: SmsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(smsList: List<SmsEntity>)

    @Update
    suspend fun update(sms: SmsEntity)

    @Query("SELECT * FROM sms ORDER BY timestamp DESC")
    fun getAllSms(): LiveData<List<SmsEntity>>

    @Query(
        """
        SELECT * FROM sms
        WHERE (:sender IS NULL OR sender LIKE :sender)
          AND (:minAmount IS NULL OR amount >= :minAmount)
          AND (:content IS NULL OR body LIKE '%' || :content || '%')
        ORDER BY timestamp DESC
    """
    )
    fun getFilteredSms(
        sender: String? = null,
        minAmount: Double? = null,
        content: String? = null
    ): LiveData<List<SmsEntity>>


    @Delete
    suspend fun delete(sms: SmsEntity)


    @Query("DELETE FROM sms")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM sms")
    suspend fun getSmsCount(): Int
}
