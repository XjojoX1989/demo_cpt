package android.lab.demo_cpt.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class Currency(
    @PrimaryKey @ColumnInfo(name = "id") val id:String,
    val name:String,
    val symbol:String
)
