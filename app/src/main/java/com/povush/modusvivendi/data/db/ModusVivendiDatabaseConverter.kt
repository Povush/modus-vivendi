package com.povush.modusvivendi.data.db

import androidx.room.TypeConverter
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.QuestType
import java.util.Date

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

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty): Int {
        return difficulty.ordinal
    }

    @TypeConverter
    fun toDifficulty(ordinal: Int): Difficulty {
        return Difficulty.entries[ordinal]
    }

    @TypeConverter
    fun fromQuestType(questType: QuestType): Int {
        return questType.ordinal
    }

    @TypeConverter
    fun toQuestType(ordinal: Int): QuestType {
        return QuestType.entries[ordinal]
    }
}