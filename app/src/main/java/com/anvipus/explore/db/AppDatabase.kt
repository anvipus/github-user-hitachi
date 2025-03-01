package com.anvipus.explore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anvipus.core.models.Users

@Database(
    entities = [
        Users::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usersDao(): UserDao

}
