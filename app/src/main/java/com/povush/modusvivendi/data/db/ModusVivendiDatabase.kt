package com.povush.modusvivendi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task

@Database(
    entities = [
        Quest::class,
        Task::class,
        Subtask::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(ModusVivendiDatabaseConverter::class)
abstract class ModusVivendiDatabase : RoomDatabase() {

    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var Instance: ModusVivendiDatabase? = null

        fun getDatabase(context: Context): ModusVivendiDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ModusVivendiDatabase::class.java, "mv_database")
                    /*TODO: Review migration*/
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}