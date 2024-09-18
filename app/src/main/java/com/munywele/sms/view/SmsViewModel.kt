package com.munywele.sms.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.database.repo.SmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SmsViewModel(private val repository: SmsRepository) : ViewModel() {
    var smsMessages: LiveData<List<SmsEntity>> = repository.getAllSms().asLiveData()


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
        minAmount: Double
    ): Flow<List<SmsEntity>> {
        return repository.getFilteredSms(sender, searchString, minAmount)
    }

    fun filterMessages(minAmount: Double, sender: String, searchString: String) {
        repository.getFilteredSms(sender, searchString, minAmount).asLiveData().let {
            smsMessages = it
        }
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