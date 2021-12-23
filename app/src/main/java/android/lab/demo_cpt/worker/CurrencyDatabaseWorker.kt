package android.lab.demo_cpt.worker

import android.content.Context
import android.lab.demo_cpt.KEY_FILENAME
import android.lab.demo_cpt.data.AppDatabase
import android.lab.demo_cpt.data.Currency
import android.lab.demo_cpt.data.CurrencyDao
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "CurrencyDatabaseWorker"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename!=null){
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<Currency>>() {}.type
                        val list: List<Currency> = Gson().fromJson(jsonReader, type)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.currencyDao().insertAll(list)

                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error currency database - invalid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error currency database", ex)
            Result.failure()
        }
    }

    private fun getCurrencyListFromFileName(filename: String?): List<Currency>? {
        if (filename == null) return null
        return applicationContext.assets.open(filename).readBytes().toString().let {
            val type = object : TypeToken<List<Currency>>() {}.type
            Gson().fromJson(it, type)
        }
    }
}
