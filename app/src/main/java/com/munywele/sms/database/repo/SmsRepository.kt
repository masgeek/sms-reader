package com.munywele.sms.database.repo

import androidx.lifecycle.LiveData
import com.munywele.sms.database.dao.SmsDao
import com.munywele.sms.database.entities.SmsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmsRepository(
    private val smsDao: SmsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getAllSms(): LiveData<List<SmsEntity>> = smsDao.getAllSms()

    fun getFilteredSms(
        sender: String? = null,
        searchString: String? = null,
        minAmount: Double? = null
    ): LiveData<List<SmsEntity>> = smsDao.getFilteredSms(
        sender = sender?.let { "%$it%" },
        content = searchString?.let { "%$it%" },
        minAmount = minAmount
    )

    suspend fun insertSms(sms: SmsEntity) = withContext(dispatcher) {
        smsDao.insert(sms)
    }

    suspend fun insertAllSms(smsList: List<SmsEntity>) = withContext(dispatcher) {
        smsDao.insertAll(smsList)
    }

    suspend fun updateSms(sms: SmsEntity) = withContext(dispatcher) {
        smsDao.update(sms)
    }

    suspend fun deleteSms(sms: SmsEntity) = withContext(dispatcher) {
        smsDao.delete(sms)
    }

    suspend fun deleteAllSms() = withContext(dispatcher) {
        smsDao.deleteAll()
    }

    suspend fun getSmsCount(): Int = withContext(dispatcher) {
        smsDao.getSmsCount()
    }
}