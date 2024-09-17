package com.munywele.sms.reader.database.repo

import com.munywele.sms.reader.database.dao.SmsDao
import com.munywele.sms.reader.database.entities.SmsEntity
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
        minAmount: Int
    ): Flow<List<SmsEntity>> {
        return smsDao.getAllSms().map { smsList ->
            smsList.filter { sms ->
                sms.address == sender
                        && sms.body.contains(searchString, ignoreCase = true)
                        && extractFirstAmountAsInt(sms.body) > minAmount
            }
        }
    }

    // Helper function to extract the amount from the SMS body
    fun extractFirstAmountAsInt(text: String): Int {
        // Regular expression to match the first amount in the format KshX,XXX.XX
        val regex = Regex("""Ksh([\d,]+\.\d{2})""")
        // Find the first match
        val matchResult = regex.find(text)?.groups?.get(1)?.value
        // Remove commas and convert to an integer (by truncating the decimal part)
        val amountFound = matchResult?.replace(",", "")?.split(".")?.first()?.toInt() ?: 0

        return amountFound
    }

    private fun extractAmountFromSms(body: String): Int {
        val regex = Regex("""Ksh[\s,]*(\d+(?:,\d{3})*(?:\.\d{2})?)""")
        val match = regex.find(body)
        val amountFound = match?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull() ?: 0

        return amountFound
    }

    suspend fun deleteAllSms() {
        smsDao.deleteAll()
    }
}