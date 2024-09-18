package com.munywele.sms.database.repo

import androidx.lifecycle.LiveData
import com.munywele.sms.database.dao.SmsDao
import com.munywele.sms.database.entities.SmsEntity

class SmsRepository(private val smsDao: SmsDao) {
    suspend fun insertSms(sms: SmsEntity) {
        smsDao.insert(sms)
    }

    fun getAllSms(): LiveData<List<SmsEntity>> {
        return smsDao.getAllSms()
    }

    fun getFilteredSms(
        sender: String?,
        searchString: String?,
        minAmount: Double?
    ): LiveData<List<SmsEntity>> {
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