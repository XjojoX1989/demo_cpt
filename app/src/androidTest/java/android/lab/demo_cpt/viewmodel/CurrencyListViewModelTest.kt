package android.lab.demo_cpt.viewmodel

import android.lab.demo_cpt.data.AppDatabase
import android.lab.demo_cpt.data.CurrencyRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidTest
class CurrencyListViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: CurrencyListViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain.outerRule(hiltRule).around(instantTaskExecutorRule)

    @Inject
    lateinit var plantRepository: CurrencyRepository

    private val mLatch = CountDownLatch(2)

    @Before
    fun setup() {
        hiltRule.inject()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        viewModel = CurrencyListViewModel(plantRepository)
        viewModel.getCurrencyListFromDB()
        mLatch.await(1, TimeUnit.SECONDS)
        mLatch.countDown()
    }

    @Test
    fun testGetCurrencyList() = runBlocking {
        Assert.assertEquals(14, viewModel.currencyList.value.size)
        Assert.assertEquals("Bitcoin", viewModel.currencyList.value[1].name)
        Assert.assertEquals("XRP", viewModel.currencyList.value[3].name)
        Assert.assertEquals("Litecoin", viewModel.currencyList.value[5].name)
        Assert.assertEquals("Chainlink", viewModel.currencyList.value[7].name)
        Assert.assertEquals("Ethereum Classic", viewModel.currencyList.value[9].name)
        Assert.assertEquals("Crypto.com Chain", viewModel.currencyList.value[11].name)
        Assert.assertEquals("USD Coin", viewModel.currencyList.value[13].name)
    }

    @Test
    fun testSortCurrencyList() = runBlocking {
        viewModel.sortListByName()
        mLatch.await(1, TimeUnit.SECONDS)
        mLatch.countDown()
        Assert.assertEquals("Binance Coin", viewModel.currencyList.value[0].name)
        Assert.assertEquals("Bitcoin Cash", viewModel.currencyList.value[2].name)
        Assert.assertEquals("Crypto.com Chain", viewModel.currencyList.value[4].name)
        Assert.assertEquals("EOS", viewModel.currencyList.value[6].name)
        Assert.assertEquals("Ethereum Classic", viewModel.currencyList.value[8].name)
        Assert.assertEquals("NEO", viewModel.currencyList.value[10].name)
        Assert.assertEquals("USD Coin", viewModel.currencyList.value[12].name)
    }
}