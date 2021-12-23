package android.lab.demo_cpt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Currency>)

    @Query("SELECT * FROM currency_table")
    fun getCurrencyList(): Flow<List<Currency>>

}