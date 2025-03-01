package com.anvipus.explore.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anvipus.core.models.Users

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBanner(list: List<Users>)

    @Query("SELECT * FROM Users")
    fun getBanners(): LiveData<List<Users>>

    @Query("DELETE FROM users")
    fun deleteAll()

}