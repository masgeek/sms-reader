package com.munywele.sms.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.database.repo.SmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.min

class SmsViewModel(private val repository: SmsRepository) : ViewModel() {

    // LiveData for observing filtered SMS messages
    private val _filteredSms = MutableLiveData<List<SmsEntity>>()
    val filteredSms: LiveData<List<SmsEntity>> get() = _filteredSms

    var smsMessages: LiveData<List<SmsEntity>> = repository.getAllSms()

    fun insertSms(sms: SmsEntity) {
        viewModelScope.launch {
            repository.insertSms(sms)
        }
    }


    fun filterMessages(
        sender: String? = null,
        minAmount: Double? = null,
        searchString: String? = null
    ) {
        repository.getFilteredSms(
            sender = sender?.let { "%$it%" },
            minAmount = minAmount,
            searchString = searchString?.let { "%$it%" }).observeForever { filteredList ->
            _filteredSms.postValue(filteredList)  // Use postValue for background updates
        }
    }

//    fun filterMessages(minAmount: Double, sender: String, searchString: String) {
//        repository.getFilteredSms(sender, searchString, minAmount).observeForever { filteredList ->
//            _filteredSms.postValue(filteredList)
//        }
//    }

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
