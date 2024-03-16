package com.example.skywatch.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skywatch.models.AlertPojo
import com.example.skywatch.models.FavoritePojo
import kotlinx.coroutines.flow.Flow

@Dao
interface SkyWatchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavLocation(favoritePojo: FavoritePojo):Long
    @Delete
    suspend fun delFavLocation(favoritePojo: FavoritePojo):Int
    @Query("SELECT * FROM FavoriteLocations")
    suspend fun getAllFavLocations(): List<FavoritePojo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlertLocation(alertPojo: AlertPojo):Long
    @Delete
    suspend fun delAlertLocation(alertPojo: AlertPojo):Int
    @Query("SELECT * FROM AlertSkyWatch")
    suspend fun getAllAlertLocations(): List<AlertPojo>
    @Query("select * from AlertSkyWatch where id = :id limit 1")
    fun getAlertWithId(id: String): AlertPojo
}