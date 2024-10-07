package com.povush.modusvivendi.data.db

import androidx.room.TypeConverter

class ModusVivendiDatabaseConverter {
    @TypeConverter
    fun fromPair(pair: Pair<Int, Int>?): String? {
        return pair?.let { "${pair.first}/${pair.second}" }
    }

    @TypeConverter
    fun toPair(value: String?): Pair<Int, Int>? {
        return value?.let {
            val parts = it.split("/")
            Pair(parts[0].toInt(), parts[1].toInt())
        }
    }
}