package android.lab.demo_cpt.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.map
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var currencyDao: CurrencyDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        currencyDao = database.currencyDao()
    }

    @Test
    fun testDatabaseWriteAndRead() {
        val currencyA = Currency("1", "AACoin", "AAC")
        val currencyB = Currency("2", "BearCoin", "BCN")
        val currencyC = Currency("3", "DogCoin", "DGC")
        currencyDao.insertAll(listOf(currencyB, currencyC, currencyA))
        val list = currencyDao.getCurrencyList()
        list.map {
            Assert.assertEquals(3, it.size)
            Assert.assertEquals(currencyB, it[0])
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

}