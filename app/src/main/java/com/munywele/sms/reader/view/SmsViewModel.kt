package com.munywele.sms.reader.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.munywele.sms.reader.database.entities.SmsEntity
import com.munywele.sms.reader.database.repo.SmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SmsViewModel(private val repository: SmsRepository) : ViewModel() {
    fun insertSms(sms: SmsEntity) {
        viewModelScope.launch {
            repository.insertSms(sms)
        }
    }

    fun getAllSms(): Flow<List<SmsEntity>> {
        return repository.getAllSms()
    }


    fun getFilteredSms(
        sender: String,
        searchString: String,
        minAmount: Int
    ): Flow<List<SmsEntity>> {
        return repository.getFilteredSms(sender, searchString, minAmount)
    }
}

class SmsViewModelFactory(private val repository: SmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}