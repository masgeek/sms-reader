package com.munywele.sms

import android.app.Application
import com.munywele.sms.database.SmsDatabase
import com.munywele.sms.database.repo.SmsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SmsReader : Application() {
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    // Initialize the database and repository
    val database by lazy { SmsDatabase.getDatabase(this) }
    val repository by lazy { SmsRepository(database.smsDao(), ioDispatcher) }
}