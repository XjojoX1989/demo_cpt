package android.lab.demo_cpt.data

import android.content.Context
import android.lab.demo_cpt.CURRENCY_DATA_FILENAME
import android.lab.demo_cpt.DATABASE_NAME
import android.lab.demo_cpt.KEY_FILENAME
import android.lab.demo_cpt.worker.CurrencyDatabaseWorker
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

/**
 * The Room database for this app
 */
@Database(entities = [Currency::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<CurrencyDatabaseWorker>()
                            .setInputData(workDataOf(KEY_FILENAME to CURRENCY_DATA_FILENAME))
                            .build()
                        WorkManager.getInstance(context).enqueue(request)
                    }

                }).build()
        }
    }
}
