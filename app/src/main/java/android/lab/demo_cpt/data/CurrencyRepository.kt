package android.lab.demo_cpt.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(private val db: CurrencyDao) {
    fun getCurrencyListFromDB(): Flow<List<Currency>> {
        return db.getCurrencyList()
    }

}
