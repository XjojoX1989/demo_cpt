package android.lab.demo_cpt.viewmodel

import android.lab.demo_cpt.data.Currency
import android.lab.demo_cpt.data.CurrencyRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@HiltViewModel
class CurrencyListViewModel @Inject constructor(private val repo: CurrencyRepository) :
    ViewModel() {

    enum class TriggerSource {
        NORMAL, SORT
    }

    private val originListFlow = repo.getCurrencyListFromDB()

    private val currencyListTrigger = MutableSharedFlow<TriggerSource>()
    val currencyList = currencyListTrigger.flatMapLatest { triggerSource ->
            if (triggerSource == TriggerSource.SORT) return@flatMapLatest originListFlow.map { list -> list.sortedBy { it.name } }
            return@flatMapLatest originListFlow
        }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, listOf())


    private val _selectedItem = MutableSharedFlow<Currency>(2)
    val selectedItem = _selectedItem.shareIn(viewModelScope, SharingStarted.Eagerly)


    fun setSelectedItem(currency: Currency) {
        viewModelScope.launch {
            _selectedItem.emit(currency)
        }
    }

    fun getCurrencyListFromDB() {
        viewModelScope.launch {
            currencyListTrigger.emit(TriggerSource.NORMAL)
        }
    }

    fun sortListByName() {
        viewModelScope.launch {
            currencyListTrigger.emit(TriggerSource.SORT)
        }
    }
}