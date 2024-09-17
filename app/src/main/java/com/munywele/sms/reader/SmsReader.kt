package com.munywele.sms.reader

import android.app.Application
import com.munywele.sms.reader.database.SmsDatabase
import com.munywele.sms.reader.database.repo.SmsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SmsReader : Application() {
    // Coroutine scope for database operations
    val applicationScope = CoroutineScope(SupervisorJob())

    // Initialize the database and repository
    private val database by lazy { SmsDatabase.getDatabase(this) }
    val repository by lazy { SmsRepository(database.smsDao()) }
}