package mgmix.dev.collection_of_android_project.calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import mgmix.dev.collection_of_android_project.calculator.dao.HistoryDao
import mgmix.dev.collection_of_android_project.calculator.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}