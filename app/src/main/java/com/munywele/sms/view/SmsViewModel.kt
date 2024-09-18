package com.munywele.sms.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.munywele.sms.database.entities.SmsEntity
import com.munywele.sms.database.repo.SmsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import kotlin.math.min

class SmsViewModel(private val repository: SmsRepository) : ViewModel() {

    // MutableStateFlow for filter parameters
    private val filterParams = MutableLiveData(FilterParams())

    // LiveData for observing filtered SMS messages
    private val _filteredSms = MutableLiveData<List<SmsEntity>>()
//    val filteredSms: LiveData<List<SmsEntity>> get() = _filteredSms

    var smsMessages: LiveData<List<SmsEntity>> = repository.getAllSms()

    // LiveData for observing filtered SMS messages
    val filteredSms: LiveData<List<SmsEntity>> = filterParams.switchMap { params ->
        repository.getFilteredSms(
            sender = params.sender?.let { "%$it%" },
            minAmount = params.minAmount,
            searchString = params.searchString?.let { "%$it%" }
        )
    }

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
        filterParams.value = FilterParams(sender, minAmount, searchString)
    }

    private data class FilterParams(
        val sender: String? = null,
        val minAmount: Double? = null,
        val searchString: String? = null
    )
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
