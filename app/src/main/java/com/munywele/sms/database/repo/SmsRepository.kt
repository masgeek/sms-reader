package com.munywele.sms.database.repo

import com.munywele.sms.database.dao.SmsDao
import com.munywele.sms.database.entities.SmsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SmsRepository(private val smsDao: SmsDao) {
    suspend fun insertSms(sms: SmsEntity) {
        smsDao.insert(sms)
    }

    fun getAllSms(): Flow<List<SmsEntity>> {
        return smsDao.getAllSms()
    }

    fun getFilteredSms(
        sender: String,
        searchString: String,
        minAmount: Double
    ): Flow<List<SmsEntity>> {
        return smsDao.getFilteredSms(
            sender = sender,
            minAmount = minAmount,
            content = searchString
        )
    }

    suspend fun deleteAllSms() {
        smsDao.deleteAll()
    }
}