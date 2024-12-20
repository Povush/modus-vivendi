package com.povush.modusvivendi.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.povush.modusvivendi.data.db.dao.QuestDao
import com.povush.modusvivendi.data.db.dao.TaskDao
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task

@Database(
    entities = [
        Quest::class,
        Task::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(ModusVivendiDatabaseConverter::class)
abstract class ModusVivendiDatabase : RoomDatabase() {

    abstract fun questDao(): QuestDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var Instance: ModusVivendiDatabase? = null

        fun getDatabase(context: Context): ModusVivendiDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ModusVivendiDatabase::class.java, "mv_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}