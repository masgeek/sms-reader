package com.munywele.sms.reader.database.repo

import com.munywele.sms.reader.database.dao.SmsDao
import com.munywele.sms.reader.database.entities.SmsEntity

class SmsRepository(private val smsDao: SmsDao) {
    suspend fun insertSms(sms: SmsEntity) {
        smsDao.insert(sms)
    }

    suspend fun getAllSms(): List<SmsEntity> {
        return smsDao.getAllSms()
    }

    suspend fun deleteAllSms() {
        smsDao.deleteAll()
    }
}